package com.aiscern.app.data.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "scan_history",
    indices = [
        Index(value = ["created_at"]),
        Index(value = ["modality"]),
        Index(value = ["verdict"])
    ]
)
data class ScanHistoryEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "modality")
    val modality: String,

    @ColumnInfo(name = "input_preview")
    val inputPreview: String,

    @ColumnInfo(name = "confidence")
    val confidence: Float,

    @ColumnInfo(name = "verdict")
    val verdict: String,

    @ColumnInfo(name = "model_scores_json")
    val modelScoresJson: String,

    @ColumnInfo(name = "heatmap_data_json")
    val heatmapDataJson: String? = null,

    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "thumbnail_path")
    val thumbnailPath: String? = null
)
