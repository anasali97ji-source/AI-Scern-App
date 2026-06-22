package com.aiscern.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiscern.app.theme.AccentGradient
import com.aiscern.app.theme.BackgroundElevated
import com.aiscern.app.theme.ErrorRed
import com.aiscern.app.theme.SuccessGreen
import com.aiscern.app.theme.TextPrimary
import com.aiscern.app.theme.WarningAmber
import com.aiscern.app.theme.AiscernTypography

@Composable
fun ConfidenceGauge(
    percentage: Float,
    modifier: Modifier = Modifier,
    size: Int = 120
) {
    var targetValue by remember { mutableFloatStateOf(0f) }
    val animatedValue by animateFloatAsState(
        targetValue = targetValue,
        animationSpec = tween(durationMillis = 1500),
        label = "gauge_animation"
    )

    LaunchedEffect(percentage) {
        targetValue = percentage
    }

    val gaugeColor = when {
        percentage < 0.5f -> SuccessGreen
        percentage < 0.7f -> WarningAmber
        else -> ErrorRed
    }

    Box(
        modifier = modifier.size(size.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size.dp)) {
            val strokeWidth = 8.dp.toPx()

            // Background track
            drawArc(
                color = BackgroundElevated,
                startAngle = -225f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Progress arc
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(gaugeColor, gaugeColor.copy(alpha = 0.7f))
                ),
                startAngle = -225f,
                sweepAngle = 270f * animatedValue,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${(animatedValue * 100).toInt()}%",
                style = AiscernTypography.displayMedium,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
        }
    }
}
