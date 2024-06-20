package com.example.skinalyze.helper


fun skinProblemMapping(skinProblem: String): Set<String> {
    val mappings = mapOf(
        "Acne-Free" to setOf("Jerawat"),
        "Anti-Aging" to setOf("Keriput", "Kantung Mata"),
        "Balancing" to setOf("Jerawat", "Komedo", "Kemerahan Kulit", "Pori-Pori"),
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
fun labelArrayToSkinProblem(label: List<String>): String {
    return when {
        "Acne-Free" in label -> "Jerawat"
        "Pore-Care" in label -> "Komedo"
        "Black-Spot" in label -> "Flek Hitam"
        "Skin-Barrier" in label -> "Normal"
        "Hydrating" in label -> "Keriput"
        else -> ""
    }

}

fun getIdFromSensitiveLabel(label: String): String {
    val mappings = mapOf(
        "Ya" to "1",
        "Tidak" to "0",
    )
    return mappings[label] ?: ""
}

fun getIdFromSkinTypeLabel(label: String): String {
    val skinTypeToId = mapOf(
        "Kombinasi" to "1",
        "Kering" to "2",
        "Normal" to "3",
        "Berminyak" to "4",
    )
    return skinTypeToId[label] ?: ""
}