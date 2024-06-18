package com.example.skinalyze.Utils

class Mapper {
    private val problemToLabelID: Map<String, String> = mapOf(
        "acnes" to "1",
        "blackheads" to "10",
        "darkspots" to "4,5",
        "normal" to "3",
        "wrinkles" to "2"
    )

    private val labelToProblem: Map<String, String> = mapOf(
        "Acne-Free" to "Acnes",
        "Pore-Care" to "Blackheads",
        "Black-Spot, Brightening" to "Darkspots",
        "Anti-Aging" to "Wrinkles",
        "Balancing" to "Normal"
    )

    private val sensitiveToId: Map<String, Int> = mapOf(
        "Ya" to 1,
        "Tidak" to 2,
    )

    private val skinTypeToId: Map<String, Int> = mapOf(
        "Kombinasi" to 1,
        "Kering" to 2,
        "Normal" to 3,
        "Berminyak" to 4,
    )

    private val reverseStringToStringMap: Map<String, String> = problemToLabelID.entries.associate { (k, v) -> v to k }

    fun getMappedValue(key: String): String? {
        return problemToLabelID[key]
    }

    fun getOriginalValue(value: String): String? {
        return reverseStringToStringMap[value]
    }

    fun getLabelIDForProblem(problem: String): String? {
        return problemToLabelID[problem]
    }

    fun getProblemForLabel(label: String): String? {
        return labelToProblem[label]
    }

    fun getAllLabelIDsForProblems(problems: List<String>): List<String> {
        return problems.mapNotNull { getLabelIDForProblem(it) }
    }

    fun getAllProblemsForLabels(labels: List<String>): List<String> {
        return labels.mapNotNull { getProblemForLabel(it) }
    }

    fun getIdFromSensitiveLabel(label: String): Int? {
        return sensitiveToId[label]
    }

    fun getIdFromSkinTypeLabel(label: String): Int? {
        return skinTypeToId[label]
    }
}