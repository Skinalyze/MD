package com.example.skinalyze.data.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse (
    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("gender")
    val gender: Int,

    @field:SerializedName("age")
    val age: Int,

    @field:SerializedName("skintype")
    val skintype: String?,

    @field:SerializedName("sensitif")
    val sensitif: Int?
)

