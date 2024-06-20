package com.example.skinalyze.data.response

import com.google.gson.annotations.SerializedName

data class PostRecommendationResponse (
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("id_rekomendasi")
    val idRekomendasi: Int
)