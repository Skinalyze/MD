package com.example.skinalyze.data.response

import com.google.gson.annotations.SerializedName

class LoginResponse (
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("token")
    val token: String? = null,

    @field:SerializedName("email")
    val email: String? = null
)
data class LoginRequest(
    val email: String,
    val password: String
)