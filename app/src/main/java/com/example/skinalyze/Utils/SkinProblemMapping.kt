package com.example.skinalyze.Utils

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
