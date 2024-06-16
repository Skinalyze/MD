package com.example.skinalyze.data.api

import com.example.skinalyze.data.request.LoginRequest
import com.example.skinalyze.data.request.RegisterRequest
import com.example.skinalyze.data.response.LoginResponse
import com.example.skinalyze.data.response.Product
import com.example.skinalyze.data.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("register")
    suspend fun register(
        @Body requestBody: RegisterRequest
    ): RegisterResponse

    @POST("login")
    suspend fun login(
        @Body requestBody: LoginRequest
    ): LoginResponse

    @GET("search")
    fun search(
        @Query("product_name") productName: String
    ): Call<List<Product>>
}