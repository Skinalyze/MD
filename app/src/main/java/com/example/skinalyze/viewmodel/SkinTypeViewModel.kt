package com.example.skinalyze.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.skinalyze.data.repository.UserRepository

class SkinTypeViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun saveSkinType(skintype: String, sensitif: String, context: Context) = userRepository.saveSkinType(skintype, sensitif, context)
}