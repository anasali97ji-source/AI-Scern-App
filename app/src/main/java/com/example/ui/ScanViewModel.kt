package com.example.ui

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.service.GeminiService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface ScanUiState {
    object Idle : ScanUiState
    object Scanning : ScanUiState
    data class Success(val result: ScanResult, val rawInput: String) : ScanUiState
    data class Error(val message: String) : ScanUiState
}

class ScanViewModel(private val repository: HistoryRepository) : ViewModel() {

    private val _scanUiState = MutableStateFlow<ScanUiState>(ScanUiState.Idle)
    val scanUiState: StateFlow<ScanUiState> = _scanUiState.asStateFlow()

    // Active state elements
    val historyState: StateFlow<List<HistoryEntity>> = repository.allHistory
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Dashboard aggregated stats
    val dashboardStats = historyState.map { list ->
        val total = list.size
        val aiCount = list.count { it.isAi }
        val aiRate = if (total > 0) (aiCount.toFloat() / total * 100) else 0f
        
        val textCount = list.count { it.type == ScanType.TEXT }
        val imageCount = list.count { it.type == ScanType.IMAGE }
        val audioCount = list.count { it.type == ScanType.AUDIO }
        val videoCount = list.count { it.type == ScanType.VIDEO }

        DashboardStats(
            totalScans = total,
            aiProbabilityAverage = aiRate,
            textScans = textCount,
            imageScans = imageCount,
            audioScans = audioCount,
            videoScans = videoCount
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardStats())

    fun triggerScan(type: ScanType, text: String, bitmap: Bitmap? = null) {
        viewModelScope.launch {
            _scanUiState.value = ScanUiState.Scanning
            try {
                val inputDescription = when (type) {
                    ScanType.TEXT -> text
                    ScanType.IMAGE -> if (text.isNotBlank()) text else "Uploaded JPEG Image Analyze"
                    ScanType.AUDIO -> if (text.isNotBlank()) text else "Captured timbers.m4a audio clip"
                    ScanType.VIDEO -> if (text.isNotBlank()) text else "Analyzed mp4 dynamic sequence"
                }

                val result = when (type) {
                    ScanType.TEXT -> MockDetectionService.detectText(inputDescription)
                    ScanType.IMAGE -> MockDetectionService.detectImage(inputDescription)
                    ScanType.AUDIO -> MockDetectionService.detectAudio(inputDescription)
                    ScanType.VIDEO -> MockDetectionService.detectVideo(inputDescription)
                }
                
                // Construct and save back into local Room DB persistence
                val entity = HistoryMapper.toEntity(type, inputDescription, result)
                repository.insert(entity)

                _scanUiState.value = ScanUiState.Success(result, inputDescription)
            } catch (e: Exception) {
                Log.e("ScanViewModel", "Scan error occurred", e)
                _scanUiState.value = ScanUiState.Error(e.localizedMessage ?: "Unknown scanning error")
            }
        }
    }

    fun resetState() {
        _scanUiState.value = ScanUiState.Idle
    }

    fun deleteItem(id: Long) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }
}

data class DashboardStats(
    val totalScans: Int = 0,
    val aiProbabilityAverage: Float = 0f,
    val textScans: Int = 0,
    val imageScans: Int = 0,
    val audioScans: Int = 0,
    val videoScans: Int = 0
)

class ScanViewModelFactory(private val repository: HistoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScanViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScanViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
