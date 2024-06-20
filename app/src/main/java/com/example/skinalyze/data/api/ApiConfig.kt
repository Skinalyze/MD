package com.example.skinalyze.data.api

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

class ApiConfig {

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

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(headerInterceptor)
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