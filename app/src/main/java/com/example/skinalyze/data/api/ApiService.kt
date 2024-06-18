package com.example.skinalyze.data.api

import com.example.skinalyze.data.request.LoginRequest
import com.example.skinalyze.data.request.RefreshRequest
import com.example.skinalyze.data.request.RegisterRequest
import com.example.skinalyze.data.request.SkinTypeRequest
import com.example.skinalyze.data.response.DetailProductResponse
import com.example.skinalyze.data.response.LoginResponse
import com.example.skinalyze.data.response.Product
import com.example.skinalyze.data.response.ProfileResponse
import com.example.skinalyze.data.response.RegisterResponse
import com.example.skinalyze.data.response.SkinTypeResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @POST("refresh-token")
    suspend fun refreshToken(
        @Body requestBody: RefreshRequest
    ): LoginResponse

    @PUT("skintype")
    suspend fun skinType(
        @Body requestBody: SkinTypeRequest
    ): SkinTypeResponse

    @GET("search")
    fun search(
        @Query("product_name") productName: String
    ): Call<List<Product>>

    @GET("search/detail")
    fun detailProduct(
        @Query("id_skin_care") idSkinCare: String
    ): Call<DetailProductResponse>

    @GET("profil")
    fun profile(): Call<ProfileResponse>

}