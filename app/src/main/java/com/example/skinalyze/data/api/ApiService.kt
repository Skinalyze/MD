package com.example.skinalyze.data.api

import com.example.skinalyze.data.request.DeleteRequest
import com.example.skinalyze.data.request.LoginRequest
import com.example.skinalyze.data.request.RecommendationRequest
import com.example.skinalyze.data.request.RefreshRequest
import com.example.skinalyze.data.request.RegisterRequest
import com.example.skinalyze.data.response.DetailProductResponse
import com.example.skinalyze.data.response.DetailRecommendationResponse
import com.example.skinalyze.data.response.LoginResponse
import com.example.skinalyze.data.response.Product
import com.example.skinalyze.data.response.ProfileResponse
import com.example.skinalyze.data.response.Recommendation
import com.example.skinalyze.data.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
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

    @POST("refresh-token")
    suspend fun refreshToken(
        @Body requestBody: RefreshRequest
    ): LoginResponse

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

    @GET("recommendation")
    fun getRecommendation(
        @Query("id_tipe_skin_type") idSkinType: Int,
        @Query("id_skin_problem") idSkinCare: String
    ): Call<List<DetailProductResponse>>

    @POST("postRecommendation")
    suspend fun postRecommendation(
        @Body requestBody: RecommendationRequest
    ): RegisterResponse

    @GET("history")
    fun getHistory(): Call<List<Recommendation>>

    @GET("history/detail")
    fun detailRecommendation(
        @Query("id_rekomendasi") idRekomendasi: String
    ): Call<DetailRecommendationResponse>

    @HTTP(method = "DELETE", path = "deleteRecommendation", hasBody = true)
    fun deleteRecommendation(
        @Body requestBody: DeleteRequest
    ): Call<RegisterResponse>

}