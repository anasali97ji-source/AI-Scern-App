package com.aiscern.app.data.repository

import com.aiscern.app.data.local.database.ScanHistoryDao
import com.aiscern.app.data.local.database.ScanHistoryEntity
import com.aiscern.app.data.remote.api.AiscernApiService
import com.aiscern.app.data.remote.dto.DetectionResponse
import com.aiscern.app.data.remote.dto.TextDetectRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

interface DetectionRepository {
    fun getHistoryStream(): Flow<List<ScanHistoryEntity>>
    suspend fun getScanById(id: String): ScanHistoryEntity?
    suspend fun detectText(text: String, ephemeral: Boolean = false): Flow<DetectResult>
    suspend fun deleteScan(id: String)
    suspend fun clearHistory()
}

sealed class DetectResult {
    data class Loading(val progress: Float = 0f) : DetectResult()
    data class Success(val response: DetectionResponse) : DetectResult()
    data class Error(val message: String) : DetectResult()
}

class DetectionRepositoryImpl(
    private val api: AiscernApiService,
    private val dao: ScanHistoryDao,
    private val json: Json
) : DetectionRepository {

    override fun getHistoryStream(): Flow<List<ScanHistoryEntity>> =
        dao.getAllScans()

    override suspend fun getScanById(id: String): ScanHistoryEntity? =
        dao.getScanById(id)

    override suspend fun detectText(text: String, ephemeral: Boolean): Flow<DetectResult> = flow {
        emit(DetectResult.Loading(0f))
        try {
            val response = api.detectText(
                apiKey = null,
                request = TextDetectRequest(text, ephemeral)
            )
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    if (!ephemeral) {
                        dao.insertScan(body.toEntity(json))
                    }
                    emit(DetectResult.Success(body))
                } else {
                    emit(DetectResult.Error("Empty response body"))
                }
            } else {
                emit(DetectResult.Error(response.errorBody()?.string() ?: "HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(DetectResult.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteScan(id: String) {
        dao.deleteScan(id)
    }

    override suspend fun clearHistory() {
        dao.clearAll()
    }
}

private fun DetectionResponse.toEntity(json: Json): ScanHistoryEntity = ScanHistoryEntity(
    id = this.id,
    modality = this.modality,
    inputPreview = this.modelScores.firstOrNull()?.modelName ?: "Text scan",
    confidence = this.confidence,
    verdict = this.verdict,
    modelScoresJson = json.encodeToString(this.modelScores),
    heatmapDataJson = this.heatmapData?.let { json.encodeToString(it) },
    isSynced = true,
    createdAt = System.currentTimeMillis()
)
