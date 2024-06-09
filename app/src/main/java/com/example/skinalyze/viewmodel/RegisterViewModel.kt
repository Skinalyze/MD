package com.example.skinalyze.viewmodel

import androidx.lifecycle.ViewModel
import com.example.skinalyze.data.repository.UserRepository

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun register(name: String, email: String, password: String, gender: String, age: Int) = userRepository.register(name, email, password, gender, age)

}