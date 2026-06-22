package com.aiscern.app.ui.screens.detect

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aiscern.app.data.repository.DetectResult
import com.aiscern.app.data.repository.DetectionRepository
import com.aiscern.app.mvi.BaseViewModel
import com.aiscern.app.mvi.UiEffect
import com.aiscern.app.mvi.UiEvent
import com.aiscern.app.mvi.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class Modality { TEXT, IMAGE, AUDIO, VIDEO }
enum class MediaType { IMAGE, AUDIO, VIDEO }

data class ScanProgress(
    val overall: Float,
    val currentModel: String,
    val completedModels: List<String>
)

data class DetectState(
    val selectedModality: Modality = Modality.TEXT,
    val textInput: String = "",
    val selectedMediaUri: Uri? = null,
    val isAnalyzing: Boolean = false,
    val scanProgress: ScanProgress? = null,
    val result: com.aiscern.app.data.remote.dto.DetectionResponse? = null,
    val error: String? = null,
    val remainingCredits: Int = 15,
    val isEphemeral: Boolean = false,
    val isDeepScan: Boolean = false
) : UiState

sealed class DetectEvent : UiEvent {
    data class OnModalitySelected(val modality: Modality) : DetectEvent()
    data class OnTextChanged(val text: String) : DetectEvent()
    data class OnMediaSelected(val uri: Uri, val type: MediaType) : DetectEvent()
    data class OnMediaCleared(val type: MediaType) : DetectEvent()
    object OnAnalyzeClicked : DetectEvent()
    data class OnEphemeralToggled(val enabled: Boolean) : DetectEvent()
    data class OnDeepScanToggled(val enabled: Boolean) : DetectEvent()
    object OnCancelClicked : DetectEvent()
    object OnRetryClicked : DetectEvent()
    data class OnShareResult(val resultId: String) : DetectEvent()
    object OnSaveToHistory : DetectEvent()
}

sealed class DetectEffect : UiEffect {
    data class ShowToast(val message: String) : DetectEffect()
    data class NavigateToResult(val resultId: String) : DetectEffect()
    data class LaunchShareSheet(val reportUri: Uri) : DetectEffect()
    data class RequestPermission(val permission: String) : DetectEffect()
}

class DetectViewModel(
    private val repository: DetectionRepository
) : BaseViewModel<DetectState, DetectEvent, DetectEffect>(DetectState()) {

    companion object {
        fun provideFactory(repository: DetectionRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return DetectViewModel(repository) as T
                }
            }
    }

    override fun onEvent(event: DetectEvent) {
        when (event) {
            is DetectEvent.OnModalitySelected -> {
                setState { copy(selectedModality = event.modality, result = null, error = null) }
            }
            is DetectEvent.OnTextChanged -> {
                setState { copy(textInput = event.text) }
            }
            is DetectEvent.OnAnalyzeClicked -> {
                when (state.value.selectedModality) {
                    Modality.TEXT -> analyzeText()
                    else -> sendEffect(DetectEffect.ShowToast("Coming in next chunk"))
                }
            }
            is DetectEvent.OnCancelClicked -> {
                setState { copy(isAnalyzing = false) }
            }
            is DetectEvent.OnDeepScanToggled -> {
                setState { copy(isDeepScan = event.enabled) }
            }
            is DetectEvent.OnEphemeralToggled -> {
                setState { copy(isEphemeral = event.enabled) }
            }
            else -> {}
        }
    }

    private fun analyzeText() {
        val text = state.value.textInput
        if (text.length < 50) {
            sendEffect(DetectEffect.ShowToast("Please enter at least 50 characters"))
            return
        }

        viewModelScope.launch {
            setState { copy(isAnalyzing = true, result = null, error = null) }

            repository.detectText(text, state.value.isEphemeral).collect { result ->
                when (result) {
                    is DetectResult.Loading -> {
                        setState { copy(scanProgress = ScanProgress(result.progress, "Analyzing...", emptyList())) }
                    }
                    is DetectResult.Success -> {
                        setState {
                            copy(
                                isAnalyzing = false,
                                result = result.response,
                                remainingCredits = remainingCredits - 1
                            )
                        }
                        sendEffect(DetectEffect.NavigateToResult(result.response.id))
                    }
                    is DetectResult.Error -> {
                        setState { copy(isAnalyzing = false, error = result.message) }
                    }
                }
            }
        }
    }
}
