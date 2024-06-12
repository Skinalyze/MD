package com.example.skinalyze.data.request

data class RegisterRequest(
    val nama: String,
    val email: String,
    val password: String,
    val gender: String,
    val age: Int
)