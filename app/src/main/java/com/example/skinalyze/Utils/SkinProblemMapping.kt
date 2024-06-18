package com.example.skinalyze.Utils

fun skinProblemMapping(skinProblem: String): Set<String> {
    val mappings = mapOf(
        "Acne-Free" to setOf("Jerawat"),
        "Anti-Aging" to setOf("Keriput", "Kantung Mata"),
        "Balancing" to setOf("Jerawat,", "Komedo", "Kemerahan Kulit", "Pori-Pori"),
        "Black-Spot" to setOf("Flek Hitam"),
        "Brightening" to setOf("Flek Hitam", "Kantung Mata"),
        "Hydrating" to setOf("Kantung Mata"),
        "Moisturizing" to setOf("Kantung Mata"),
        "No-Whitecast" to setOf("Pori-Pori"),
        "Oil-Control" to setOf("Jerawat", "Komedo", "Pori-Pori"),
        "Pore-Care" to setOf("Jerawat", "Komedo", "Pori-Pori"),
        "Refreshing" to setOf("Kantung Mata"),
        "Skin-Barrier" to setOf("Flek Hitam", "Keriput", "Kemerahan Kulit"),
        "Soothing" to setOf("Jerawat", "Kemerahan Kulit"),
        "UV-Protection" to setOf("Jerawat", "Komedo", "Flek Hitam", "Keriput", "Kemerahan Kulit", "Pori-Pori", "Kantung Mata")
    )

    return mappings[skinProblem] ?: emptySet()
}
