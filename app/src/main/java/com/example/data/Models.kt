package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

enum class ScanType {
    TEXT, IMAGE, AUDIO, VIDEO
}

data class ModelBreakdown(
    val syntacticAnomaly: Float,      // % for text, or compression artifact % for image/video, or compression % etc.
    val semanticConsistency: Float,   // %
    val repetitionPattern: Float       // %
)

data class ScanResult(
    val isAi: Boolean,
    val score: Float,                  // Percentage 0-100 indicating likelihood of AI origin
    val confidence: Float,             // Percentage 0-100 indicating scanning confidence
    val breakdown: ModelBreakdown,
    val analysisText: String,
    val metadataInfo: String = ""       // Optional extra info like length, image sizes, language type
)

@Entity(tableName = "scan_history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: ScanType,
    val inputTextOrPath: String,        // Sourced text or URI/Description of media scanned 
    val isAi: Boolean,
    val score: Float,
    val confidence: Float,
    val syntacticAnomaly: Float,
    val semanticConsistency: Float,
    val repetitionPattern: Float,
    val analysisText: String,
    val timestamp: Long = System.currentTimeMillis()
)

// Simple converter for Room Database if needed or helper mappings
object HistoryMapper {
    fun fromEntity(entity: HistoryEntity): ScanResult {
        return ScanResult(
            isAi = entity.isAi,
            score = entity.score,
            confidence = entity.confidence,
            breakdown = ModelBreakdown(
                syntacticAnomaly = entity.syntacticAnomaly,
                semanticConsistency = entity.semanticConsistency,
                repetitionPattern = entity.repetitionPattern
            ),
            analysisText = entity.analysisText,
            metadataInfo = ""
        )
    }

    fun toEntity(type: ScanType, value: String, result: ScanResult): HistoryEntity {
        return HistoryEntity(
            type = type,
            inputTextOrPath = value,
            isAi = result.isAi,
            score = result.score,
            confidence = result.confidence,
            syntacticAnomaly = result.breakdown.syntacticAnomaly,
            semanticConsistency = result.breakdown.semanticConsistency,
            repetitionPattern = result.breakdown.repetitionPattern,
            analysisText = result.analysisText
        )
    }
}
