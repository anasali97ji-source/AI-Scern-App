package com.aiscern.app.ui.screens.result

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiscern.app.R
import com.aiscern.app.theme.BackgroundPrimary
import com.aiscern.app.theme.BackgroundSecondary
import com.aiscern.app.theme.ErrorRed
import com.aiscern.app.theme.SuccessGreen
import com.aiscern.app.theme.TextMuted
import com.aiscern.app.theme.TextPrimary
import com.aiscern.app.theme.TextSecondary
import com.aiscern.app.theme.WarningAmber
import com.aiscern.app.theme.AiscernTypography
import com.aiscern.app.ui.components.AiscernCard
import com.aiscern.app.ui.components.AiscernPrimaryButton
import com.aiscern.app.ui.components.AiscernSecondaryButton
import com.aiscern.app.ui.components.AiscernTopAppBar
import com.aiscern.app.ui.components.ConfidenceGauge

@Composable
fun ResultScreen(
    scanId: String,
    onBack: () -> Unit,
    onNewScan: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Mock data for demonstration - in production, fetch from repository
    val mockResult = remember {
        ResultData(
            id = scanId,
            confidence = 0.87f,
            verdict = Verdict.AI_GENERATED,
            modelScores = listOf(
                ModelScoreData("RoBERTa", 0.92f),
                ModelScoreData("Binoculars", 0.85f),
                ModelScoreData("Gemini 2.0", 0.88f),
                ModelScoreData("ViT Classifier", 0.79f),
                ModelScoreData("CLIP", 0.83f),
                ModelScoreData("wav2vec2", 0.91f),
                ModelScoreData("Spectral", 0.87f),
                ModelScoreData("NVIDIA NIM", 0.90f)
            ),
            timestamp = "June 22, 2026 at 11:30 PM"
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundPrimary)
    ) {
        AiscernTopAppBar(
            title = stringResource(R.string.result_title),
            onBackClick = onBack,
            actions = {
                IconButton(onClick = { /* Share */ }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = TextSecondary
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
        ) {
            item { VerdictSection(result = mockResult) }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { ModelBreakdownSection(modelScores = mockResult.modelScores) }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { ActionButtons(onNewScan = onNewScan) }
        }
    }
}

@Composable
private fun VerdictSection(
    result: ResultData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConfidenceGauge(
            percentage = result.confidence,
            size = 140
        )

        Spacer(modifier = Modifier.height(16.dp))

        val verdictText = when (result.verdict) {
            Verdict.AI_GENERATED -> stringResource(R.string.result_verdict_ai)
            Verdict.HUMAN -> stringResource(R.string.result_verdict_human)
            Verdict.UNCERTAIN -> stringResource(R.string.result_verdict_uncertain)
        }

        val verdictColor = when (result.verdict) {
            Verdict.AI_GENERATED -> ErrorRed
            Verdict.HUMAN -> SuccessGreen
            Verdict.UNCERTAIN -> WarningAmber
        }

        Text(
            text = verdictText,
            style = AiscernTypography.headlineMedium,
            color = verdictColor,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        val confidenceLabel = when {
            result.confidence > 0.8f -> stringResource(R.string.result_confidence_high)
            result.confidence > 0.5f -> stringResource(R.string.result_confidence_medium)
            else -> "Low confidence"
        }

        Text(
            text = confidenceLabel,
            style = AiscernTypography.labelLarge,
            color = TextMuted,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = result.timestamp,
            style = AiscernTypography.labelMedium,
            color = TextMuted,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ModelBreakdownSection(
    modelScores: List<ModelScoreData>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(true) }

    AiscernCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.result_model_breakdown),
                    style = AiscernTypography.titleLarge,
                    color = TextPrimary
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = TextSecondary
                )
            }

            if (expanded) {
                Column(
                    modifier = Modifier.animateContentSize()
                ) {
                    modelScores.forEach { model ->
                        ModelScoreRow(model = model)
                    }
                }
            }
        }
    }
}

@Composable
private fun ModelScoreRow(
    model: ModelScoreData,
    modifier: Modifier = Modifier
) {
    val barColor = when {
        model.score < 0.3f -> SuccessGreen
        model.score < 0.7f -> WarningAmber
        else -> ErrorRed
    }

    val animatedProgress by animateFloatAsState(
        targetValue = model.score,
        animationSpec = tween(1000),
        label = "score_animation"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = model.name,
            style = AiscernTypography.bodyMedium,
            color = TextSecondary,
            modifier = Modifier.width(120.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(BackgroundSecondary)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(barColor)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "${(model.score * 100).toInt()}%",
            style = AiscernTypography.labelMedium,
            color = TextPrimary,
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun ActionButtons(
    onNewScan: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AiscernPrimaryButton(
            text = stringResource(R.string.result_action_save),
            onClick = { /* Save to history */ },
            modifier = Modifier.fillMaxWidth()
        )
        AiscernSecondaryButton(
            text = stringResource(R.string.result_action_share),
            onClick = { /* Share */ },
            modifier = Modifier.fillMaxWidth()
        )
        AiscernSecondaryButton(
            text = stringResource(R.string.result_action_new),
            onClick = onNewScan,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// Data classes for UI
data class ResultData(
    val id: String,
    val confidence: Float,
    val verdict: Verdict,
    val modelScores: List<ModelScoreData>,
    val timestamp: String
)

data class ModelScoreData(
    val name: String,
    val score: Float
)

enum class Verdict {
    AI_GENERATED, HUMAN, UNCERTAIN
}
