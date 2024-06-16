package com.example.skinalyze.pref

data class UserModel(
    val accessToken: String,
    val refreshToken: String,
    val idUser: String,
    val isLogin: Boolean = false
)