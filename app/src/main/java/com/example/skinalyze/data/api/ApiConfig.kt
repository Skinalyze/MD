package com.example.skinalyze.data.api

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.skinalyze.BuildConfig
import com.example.skinalyze.data.request.RefreshRequest
import com.example.skinalyze.pref.UserPreference
import com.example.skinalyze.pref.dataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.CertificateException
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class ApiConfig(private val context: Context) {

    companion object {
        fun getApiService(context: Context): ApiService {
            val loggingInterceptor = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

            val userPreference = UserPreference.getInstance(context.dataStore)
            val token = runBlocking {
                userPreference.getSession().map { it.accessToken }.firstOrNull()
            }

            Log.d("DEBUG CONFIG", token.toString())

            val headerInterceptor = Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                val response = chain.proceed(request)

                if (response.code == 401) {
                    synchronized(this) {
                        val newToken = runBlocking {
                            refreshToken(context, userPreference)
                        }
                        if (newToken != null) {
                            runBlocking {
                                userPreference.refreshToken(newToken)
                            }
                            val newRequest = request.newBuilder()
                                .removeHeader("Authorization")
                                .addHeader("Authorization", "Bearer $newToken")
                                .build()
                            response.close()
                            chain.proceed(newRequest)
                        } else {
                            response
                        }
                    }
                } else {
                    response
                }
            }

            // NEEDS TO BE DELETED FOR PRODUCTION
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                }

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                    return arrayOf()
                }
            })

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())

            val sslSocketFactory = sslContext.socketFactory

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(headerInterceptor)
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true }
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }

        private suspend fun refreshToken(context: Context, userPreference: UserPreference): String? {
            return try {
                val refreshToken = userPreference.getSession().map { it.refreshToken }.firstOrNull()
                if (!refreshToken.isNullOrEmpty()) {
                    val apiService = getApiService(context)
                    val response = apiService.refreshToken(RefreshRequest(refreshToken))
                    response.accessToken
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }
}