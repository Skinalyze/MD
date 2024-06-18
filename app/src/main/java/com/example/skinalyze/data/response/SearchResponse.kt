package com.example.skinalyze.data.response

import com.google.gson.annotations.SerializedName

data class Product(
    @field:SerializedName("id_skin_care")
    val idSkinCare: String,

    @field:SerializedName("product_name")
    val productName: String,

    @field:SerializedName("brand")
    val brand: String,

    @field:SerializedName("foto")
    val foto: String,

    @field:SerializedName("picture_src")
    val img: String,
)