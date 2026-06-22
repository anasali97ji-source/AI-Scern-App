package com.aiscern.app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TextDetectRequest(
    val text: String,
    val ephemeral: Boolean = false,
    val language: String? = null
)

@Serializable
data class FeedbackRequest(
    val scanId: String,
    val isCorrect: Boolean,
    val notes: String? = null
)

@Serializable
data class DetectionResponse(
    val id: String,
    val modality: String,
    val confidence: Float,
    val verdict: String,
    val modelScores: List<ModelScoreDto>,
    val heatmapData: HeatmapDataDto? = null,
    val ragEvidence: List<RagEvidenceDto>? = null,
    val processingTime: Int,
    val creditsUsed: Int,
    val createdAt: String
)

@Serializable
data class ModelScoreDto(
    val modelName: String,
    val score: Float,
    val weight: Float
)

@Serializable
data class HeatmapDataDto(
    val type: String,
    val segments: List<HeatmapSegmentDto>
)

@Serializable
data class HeatmapSegmentDto(
    val start: Int,
    val end: Int,
    val confidence: Float,
    val label: String
)

@Serializable
data class RagEvidenceDto(
    val patternName: String,
    val similarity: Float,
    val description: String
)

@Serializable
data class HistoryResponse(
    val scans: List<HistoryItemDto>,
    val total: Int,
    val page: Int
)

@Serializable
data class HistoryItemDto(
    val id: String,
    val modality: String,
    val inputPreview: String,
    val confidence: Float,
    val verdict: String,
    val createdAt: String,
    val thumbnailUrl: String? = null
)

@Serializable
data class CreditResponse(
    val remaining: Int,
    val total: Int,
    val resetsAt: String,
    val plan: String
)
