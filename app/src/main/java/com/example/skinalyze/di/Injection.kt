package com.example.skinalyze.di

import android.content.Context
import com.example.skinalyze.data.api.ApiConfig
import com.example.skinalyze.data.repository.UserRepository

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val api = ApiConfig.getApiService()
        return UserRepository.getInstance(api)
    }
}