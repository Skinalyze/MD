package com.example.skinalyze.data.api

import com.example.skinalyze.data.request.LoginRequest
import com.example.skinalyze.data.request.RegisterRequest
import com.example.skinalyze.data.response.LoginResponse
import com.example.skinalyze.data.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Body requestBody: RegisterRequest
    ): RegisterResponse

    @POST("login")
    suspend fun login(
        @Body requestBody: LoginRequest
    ): LoginResponse
}