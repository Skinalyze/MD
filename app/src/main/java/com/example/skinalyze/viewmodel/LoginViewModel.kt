package com.example.skinalyze.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skinalyze.data.repository.UserRepository
import com.example.skinalyze.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun login(email: String, password: String) = userRepository.login(email, password)
}