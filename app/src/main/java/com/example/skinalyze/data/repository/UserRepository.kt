package com.example.skinalyze.data.repository

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.skinalyze.data.api.ApiConfig
import com.example.skinalyze.data.api.ApiService
import com.example.skinalyze.data.request.LoginRequest
import com.example.skinalyze.data.request.RegisterRequest
import com.example.skinalyze.data.request.SkinTypeRequest
import com.example.skinalyze.data.response.LoginResponse
import com.example.skinalyze.data.response.Product
import com.example.skinalyze.data.response.ProfileResponse
import com.example.skinalyze.data.response.RegisterResponse
import com.example.skinalyze.data.response.SkinTypeResponse
import com.example.skinalyze.pref.UserModel
import com.example.skinalyze.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    private val _profile = MutableLiveData<Result<ProfileResponse>>()
    val profile: LiveData<Result<ProfileResponse>> get() = _profile

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    fun register(nama: String, email: String, password: String, gender: String, age: Int) : LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val registerRequest = RegisterRequest(nama, email, password, gender, age)
            val successResponse = apiService.register(registerRequest)
            emit(Result.Success(successResponse))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }

    }

    fun login(email: String, password:String) : LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val loginRequest = LoginRequest(email, password)
            val successResponse = apiService.login(loginRequest)
            emit(Result.Success(successResponse))
            val accessToken = successResponse.accessToken.toString()
            val refreshToken = successResponse.refreshToken.toString()
            val idUser = successResponse.idUser.toString()
            saveSession(UserModel(accessToken, refreshToken, idUser, true))
            Log.d("DEBUG", getSession().toString())
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun saveSkinType(skintypes: String, sensitif: String, context: Context) : LiveData<Result<SkinTypeResponse>> = liveData {
        emit(Result.Loading)
        try {
            val skinTypeRequest = SkinTypeRequest(skintypes, sensitif)
            val successResponse = ApiConfig.getApiService(context).skinType(skinTypeRequest)
            emit(Result.Success(successResponse))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getProfile(context: Context) : LiveData<Result<ProfileResponse>> = liveData {
        _profile.value = Result.Loading
        val client = ApiConfig.getApiService(context).profile()
        client.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>, response: Response<ProfileResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _profile.value = Result.Success(it)
                    }
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }

        })
//        client.enqueue(object : Callback<ProfileResponse> {
//            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
//                if (response.isSuccessful) {
//                    response.body()?.let {
//                        _profile.value = Result.Success(it)
//                    } ?: run {
//                        Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
//                    }
//                } else {
//                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
//                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
//            }
//        })
    }



    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}