package com.example.skinalyze.data.api

import com.example.skinalyze.data.response.LoginResponse
import com.example.skinalyze.data.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("nama") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("gender") gender: String,
        @Field("age") age: Int
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse
}