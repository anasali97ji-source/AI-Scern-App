package com.aiscern.app.ui.screens.detect.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiscern.app.theme.*
import com.aiscern.app.ui.components.AiscernSecondaryButton
import kotlin.random.Random

@Composable
fun ScanningAnimation(
    progress: Float,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val particles = remember { List(40) { Particle.random() } }
    val infiniteTransition = rememberInfiniteTransition(label = "particle")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundPrimary.copy(alpha = 0.95f)),
        contentAlignment = Alignment.Center
    ) {
        // Particle network background
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            particles.forEach { particle ->
                val x = (particle.baseX + kotlin.math.sin(time * particle.speed + particle.phase) * 30).toFloat()
                val y = (particle.baseY + kotlin.math.cos(time * particle.speed + particle.phase) * 30).toFloat()

                drawCircle(
                    color = AccentViolet.copy(alpha = 0.3f),
                    radius = 2.dp.toPx(),
                    center = Offset(x, y)
                )

                // Draw connections
                particles.forEach { other ->
                    val ox = (other.baseX + kotlin.math.sin(time * other.speed + other.phase) * 30).toFloat()
                    val oy = (other.baseY + kotlin.math.cos(time * other.speed + other.phase) * 30).toFloat()
                    val distance = kotlin.math.hypot((x - ox).toDouble(), (y - oy).toDouble())

                    if (distance < 100) {
                        val alpha = (1 - distance / 100).toFloat() * 0.1f
                        drawLine(
                            color = AccentViolet.copy(alpha = alpha),
                            start = Offset(x, y),
                            end = Offset(ox, oy),
                            strokeWidth = 1f
                        )
                    }
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            // Circular progress
            Box(
                modifier = Modifier.size(150.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    color = AccentViolet,
                    trackColor = BackgroundElevated,
                    strokeWidth = 8.dp
                )
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = AiscernTypography.displayMedium,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Analyzing with 8 models…",
                style = AiscernTypography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "This may take 10–30 seconds",
                style = AiscernTypography.bodyMedium,
                color = TextMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            AiscernSecondaryButton(
                text = "Stop Analysis",
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth(0.6f)
            )
        }
    }
}

private data class Particle(
    val baseX: Float,
    val baseY: Float,
    val speed: Float,
    val phase: Float
) {
    companion object {
        fun random(): Particle = Particle(
            baseX = Random.nextFloat() * 1080f,
            baseY = Random.nextFloat() * 1920f,
            speed = 0.5f + Random.nextFloat() * 1.5f,
            phase = Random.nextFloat() * kotlin.math.PI.toFloat() * 2f
        )
    }
}
