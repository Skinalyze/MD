package com.example.skinalyze.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.skinalyze.data.api.ApiService
import com.example.skinalyze.data.response.RegisterResponse

class UserRepository private constructor(
    private val apiService: ApiService
) {

    fun register(nama: String, email: String, password: String, gender: String, age: Int) : LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val successResponse = apiService.register(nama, email, password, gender, age)
            emit(Result.Success(successResponse))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }

    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }
}