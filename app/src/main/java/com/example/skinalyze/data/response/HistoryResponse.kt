package com.example.skinalyze.data.response

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Recommendation(
    @field: SerializedName("id_rekomendasi")
    val id: Int,

    @field: SerializedName("sensitif")
    val isSensitive: Int,

    @field: SerializedName("skin_type")
    val skinType: String,

    @field: SerializedName("skin_problem")
    val skinProblem: String,

    @field: SerializedName("timestamp")
    val timestamp: Date
)