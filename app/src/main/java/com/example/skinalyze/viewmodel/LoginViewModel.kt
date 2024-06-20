package com.example.skinalyze.viewmodel

import androidx.lifecycle.ViewModel
import com.example.skinalyze.data.repository.UserRepository

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun login(email: String, password: String) = userRepository.login(email, password)
}