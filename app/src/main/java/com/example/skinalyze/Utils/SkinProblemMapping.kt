package com.example.skinalyze.Utils

import android.util.Log

fun skinProblemMapping(skinProblem: String): String {
    val mappings = mapOf(
        "Acne-Free" to "Jerawat",
        "Anti-Aging" to "Keriput, Kantung Mata",
        "Balancing" to "Jerawat, Komedo, Kemerahan Kulit, Pori-Pori",
        "Black-Spot" to "Flek Hitam",
        "Brightening" to "Flek Hitam, Kantung Mata",
        "Hydrating" to "Kantung Mata",
        "Moisturizing" to "Kantung Mata",
        "No-Whitecast" to "Pori-Pori",
        "Oil-Control" to "Jerawat, Komedo, Pori-Pori",
        "Pore-Care" to "Jerawat, Komedo, Pori-Pori",
        "Refreshing" to "Kantung Mata",
        "Skin-Barrier" to "Flek Hitam, Keriput, Kemerahan Kulit",
        "Soothing" to "Jerawat, Kemerahan Kulit",
        "UV-Protection" to "Jerawat, Komedo, Flek Hitam, Keriput, Kemerahan Kulit, Pori-Pori, Kantung Mata"
    )

    return mappings[skinProblem] ?: ""
}

fun skinTypeMapping(skinProblem: String): Int {
    val mappings = mapOf(
        "Combination" to 1,
        "Dry" to 2,
        "Normal" to 3,
        "Oily" to 4
    )

    return mappings[skinProblem] ?: 0
}

fun skinTypeTranslate(skinProblem: String): String {
    val mappings = mapOf(
        "Combination" to "Kombinasi",
        "Dry" to "Kering",
        "Normal" to "Normal",
        "Oily" to "Berminyak",
        "Sensitif" to "Sensitif"
    )

    return mappings[skinProblem] ?: ""
}

fun skinProblemToLabel(skinProblem: String): String {
    val mappings = mapOf(
        "acnes" to "1,3,10,13",
        "blackheads" to "3,9,10",
        "darkspots" to "2,4,5",
        "normal" to "3,7,12",
        "wrinkles" to "2,6,7"
    )
    return mappings[skinProblem] ?: ""
}

fun labelToSkinProblem(label: String) : String{
    val mappings = mapOf(
        "Acne-Free, Balancing, Pore-Care, Soothing" to "Jerawat",
        "Balancing, Oil-Control, Pore-Care" to "Komedo",
        "Anti-Aging, Black-Spot, Brightening" to "Flek Hitam",
        "Balancing, Moisturizing, Skin-Barrier" to "Normal",
        "Anti-Aging, Hydrating, Moisturizing" to "Keriput"
    )
    return mappings[label] ?: ""
}
