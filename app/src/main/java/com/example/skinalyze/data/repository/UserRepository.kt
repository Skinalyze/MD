package com.example.skinalyze.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.skinalyze.data.api.ApiConfig
import com.example.skinalyze.data.api.ApiService
import com.example.skinalyze.data.response.LoginResponse
import com.example.skinalyze.data.response.RegisterResponse
import com.example.skinalyze.pref.UserModel
import com.example.skinalyze.pref.UserPreference
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    fun register(nama: String, email: String, password: String, gender: String, age: Int) : LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val successResponse = apiService.register(nama, email, password, gender, age)
            emit(Result.Success(successResponse))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }

    }

    fun login(email: String, password:String) : LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val successResponse = apiService.login(email, password)
            emit(Result.Success(successResponse))
            saveSession(UserModel(email, successResponse.token.toString()))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
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