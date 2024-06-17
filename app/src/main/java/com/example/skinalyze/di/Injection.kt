package com.example.skinalyze.di

import android.content.Context
import com.example.skinalyze.data.api.ApiConfig
import com.example.skinalyze.data.repository.UserRepository
import com.example.skinalyze.pref.UserPreference
import com.example.skinalyze.pref.dataStore

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val api = ApiConfig.getApiService(context)
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(api, pref)
    }
}