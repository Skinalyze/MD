package com.example.skinalyze.data.response

import com.google.gson.annotations.SerializedName

data class RecommendationsResponse(
    val products: List<SkincareProduct>
)

data class SkincareProduct(
    @SerializedName("id_skin_care")
    val idSkinCare: Int,

    @SerializedName("id_product_type")
    val idProductType: Int,

    @SerializedName("product_name")
    val productName: String,

    @SerializedName("product_href")
    val productHref: String,

    @SerializedName("brand")
    val brand: String,

    @SerializedName("price")
    val price: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("sensitif")
    val sensitif: Int,

    @SerializedName("picture_src")
    val pictureSrc: String,

    @SerializedName("id_tipe_skin_type")
    val idTipeSkinType: Int,

    @SerializedName("id_skin_problem")
    val idSkinProblem: Int
)