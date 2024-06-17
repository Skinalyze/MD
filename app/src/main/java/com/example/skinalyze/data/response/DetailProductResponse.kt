package com.example.skinalyze.data.response

import com.google.gson.annotations.SerializedName

data class DetailProductResponse(

	@field:SerializedName("product_type")
	val productType: String? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("notable_effects")
	val notableEffects: List<String?>? = null,

	@field:SerializedName("sensitif")
	val sensitif: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("picture_src")
	val pictureSrc: String? = null,

	@field:SerializedName("skin_type")
	val skinType: List<String?>? = null,

	@field:SerializedName("product_name")
	val productName: String? = null,

	@field:SerializedName("brand")
	val brand: String? = null
)
