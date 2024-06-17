package com.example.skinalyze.data.response

import com.google.gson.annotations.SerializedName


data class DetailRecommendationResponse(
    @field: SerializedName("timestamp")
    val timestamp: String,

    @field: SerializedName("userId")
    val userId: Int,

    @field: SerializedName("skin_type")
    val skinType: List<String>,

    @field:SerializedName("skin_problem")
    val skinProblem: List<String>,

    @SerializedName("skin_care")
    val skinCare: List<Product>
)