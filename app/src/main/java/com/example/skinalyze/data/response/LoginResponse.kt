package com.example.skinalyze.data.response

import com.google.gson.annotations.SerializedName

class LoginResponse (
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("id_user")
    val idUser: Int? = null,

    @field:SerializedName("access_token")
    val accessToken: String? = null,

    @field:SerializedName("refresh_token")
    val refreshToken: String? = null
)