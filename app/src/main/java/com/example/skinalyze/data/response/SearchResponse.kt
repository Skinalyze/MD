package com.example.skinalyze.data.response

import com.google.gson.annotations.SerializedName

data class Product(
    @field:SerializedName("id_skin_care")
    val idSkinCare: Int,

    @field:SerializedName("product_name")
    val productName: String,

    @field:SerializedName("brand")
    val brand: String,

    @field:SerializedName("foto")
    val foto: String,
)