package com.aiscern.app.ui.screens.detect

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aiscern.app.R
import com.aiscern.app.theme.BackgroundPrimary
import com.aiscern.app.theme.TextMuted
import com.aiscern.app.theme.AiscernTypography
import com.aiscern.app.ui.components.AiscernTopAppBar
import com.aiscern.app.ui.screens.detect.components.AudioRecorderPanel
import com.aiscern.app.ui.screens.detect.components.ImagePickerPanel
import com.aiscern.app.ui.screens.detect.components.ModalitySelector
import com.aiscern.app.ui.screens.detect.components.ScanningAnimation
import com.aiscern.app.ui.screens.detect.components.TextInputPanel
import com.aiscern.app.ui.screens.detect.components.VideoPickerPanel

@Composable
fun DetectScreen(
    viewModel: DetectViewModel,
    onResultReady: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val effect by viewModel.effect.collectAsState(initial = null)

    LaunchedEffect(effect) {
        when (effect) {
            is DetectEffect.NavigateToResult -> {
                onResultReady((effect as DetectEffect.NavigateToResult).resultId)
            }
            else -> {}
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundPrimary)
    ) {
        AiscernTopAppBar(
            title = stringResource(R.string.detect_title)
        )

        // Credit display
        Text(
            text = stringResource(R.string.detect_credits_remaining, state.remainingCredits),
            style = AiscernTypography.labelMedium,
            color = TextMuted,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )

        // Modality selector
        ModalitySelector(
            selectedModality = state.selectedModality,
            onModalitySelected = { viewModel.onEvent(DetectEvent.OnModalitySelected(it)) },
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Modality panel
        AnimatedContent(
            targetState = state.selectedModality,
            transitionSpec = {
                (slideInHorizontally { it } + fadeIn(tween(300)))
                    .togetherWith(slideOutHorizontally { -it } + fadeOut(tween(300)))
            },
            label = "modality_switch"
        ) { modality ->
            when (modality) {
                Modality.TEXT -> TextInputPanel(
                    text = state.textInput,
                    onTextChange = { viewModel.onEvent(DetectEvent.OnTextChanged(it)) },
                    onAnalyze = { viewModel.onEvent(DetectEvent.OnAnalyzeClicked) },
                    isAnalyzing = state.isAnalyzing,
                    modifier = Modifier.fillMaxSize()
                )
                Modality.IMAGE -> ImagePickerPanel(
                    selectedUri = state.selectedMediaUri,
                    onImageSelected = { uri ->
                        viewModel.onEvent(DetectEvent.OnMediaSelected(uri, MediaType.IMAGE))
                    },
                    onImageCleared = {
                        viewModel.onEvent(DetectEvent.OnMediaCleared(MediaType.IMAGE))
                    },
                    onAnalyze = { viewModel.onEvent(DetectEvent.OnAnalyzeClicked) },
                    isDeepScan = state.isDeepScan,
                    onDeepScanToggle = { viewModel.onEvent(DetectEvent.OnDeepScanToggled(it)) },
                    modifier = Modifier.fillMaxSize()
                )
                Modality.AUDIO -> AudioRecorderPanel(
                    onAnalyze = { viewModel.onEvent(DetectEvent.OnAnalyzeClicked) },
                    modifier = Modifier.fillMaxSize()
                )
                Modality.VIDEO -> VideoPickerPanel(
                    onAnalyze = { viewModel.onEvent(DetectEvent.OnAnalyzeClicked) },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    // Scanning overlay
    if (state.isAnalyzing) {
        ScanningAnimation(
            progress = state.scanProgress?.overall ?: 0f,
            onCancel = { viewModel.onEvent(DetectEvent.OnCancelClicked) },
            modifier = Modifier.fillMaxSize()
        )
    }
}
