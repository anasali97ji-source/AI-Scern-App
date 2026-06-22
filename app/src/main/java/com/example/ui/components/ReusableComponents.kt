package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ScanType
import com.example.ui.theme.*

// 1. AuroraBackground
@Composable
fun AuroraBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceBackground),
        content = {
            // Draw aurora effect
            Canvas(modifier = Modifier.fillMaxSize()) {
                val color1 = Color(0xFF00D4FF).copy(alpha = 0.05f) // Electric Cyan
                val color2 = Color(0xFF6001D1).copy(alpha = 0.05f) // Deep Violet
                val color3 = Color(0xFFFF69B4).copy(alpha = 0.03f) // Pink

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(color1, Color.Transparent),
                        center = Offset(size.width * 0.2f, size.height * 0.3f),
                        radius = size.width * 0.7f
                    )
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(color2, Color.Transparent),
                        center = Offset(size.width * 0.8f, size.height * 0.5f),
                        radius = size.width * 0.6f
                    )
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(color3, Color.Transparent),
                        center = Offset(size.width * 0.5f, size.height * 0.8f),
                        radius = size.width * 0.8f
                    )
                )
            }
            content()
        }
    )
}

// 2. ConfidenceGauge
@Composable
fun ConfidenceGauge(
    confidence: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(60.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Background track
            drawCircle(
                color = Color.White.copy(alpha = 0.1f),
                radius = size.minDimension / 2f,
                style = Stroke(width = 6.dp.toPx())
            )
            // Progress
            drawArc(
                color = ElectricCyan,
                startAngle = -90f,
                sweepAngle = confidence * 3.6f,
                useCenter = false,
                style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Text(
            text = "${confidence.toInt()}%",
            color = SoftWhite,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// 3. VerdictBadge
@Composable
fun VerdictBadge(
    isAi: Boolean,
    modifier: Modifier = Modifier
) {
    val color = if (isAi) AlertRed else SuccessGreen
    val label = if (isAi) "AI GENERATED" else "HUMAN"
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.15f))
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
        )
    }
}

// 4. ScanCard
@Composable
fun ScanCard(
    label: String,
    sublabel: String,
    icon: ImageVector,
    tintColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(115.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                if (isSelected) tintColor.copy(alpha = 0.12f) else CardSlate.copy(alpha = 0.5f)
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                brush = if (isSelected) Brush.linearGradient(listOf(Color(0xFF2563EB), Color(0xFF9333EA))) else Brush.linearGradient(listOf(Color.White.copy(alpha = 0.05f), Color.White.copy(alpha = 0.05f))),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(tintColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = tintColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Text(
                    text = label,
                    color = SoftWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = sublabel,
                    color = TextGray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1
                )
            }
        }
    }
}

// 5. ModalityIcon
fun getModalityIcon(type: ScanType): ImageVector {
    return when (type) {
        ScanType.TEXT -> Icons.Default.TextFields
        ScanType.IMAGE -> Icons.Default.Image
        ScanType.AUDIO -> Icons.Default.Mic
        ScanType.VIDEO -> Icons.Default.Videocam
    }
}

// 6. AnimatedButton
@Composable
fun AnimatedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.95f else 1f, label = "button_scale")
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current

    Box(
        modifier = modifier
            .scale(scale)
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (enabled) Brush.linearGradient(listOf(Color(0xFF2563EB), Color(0xFF9333EA)))
                else Brush.linearGradient(listOf(Color.DarkGray, Color.Gray))
            )
            .pointerInput(enabled) {
                if (enabled) {
                    awaitPointerEventScope {
                        while (true) {
                            awaitFirstDown(requireUnconsumed = false)
                            isPressed = true
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                            waitForUpOrCancellation()
                            isPressed = false
                            onClick()
                        }
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

// 7. EmptyState
@Composable
fun EmptyState(
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Inbox,
            contentDescription = "Empty",
            tint = TextGray.copy(alpha = 0.5f),
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            color = TextGray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// 8. ResultRow
@Composable
fun ResultRow(
    type: ScanType,
    score: Float,
    isAi: Boolean,
    title: String,
    dateString: String,
    onDelete: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(CardSlate.copy(alpha = 0.5f))
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Modality Icon Wrapper
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            when (type) {
                                ScanType.TEXT -> ElectricCyan.copy(alpha = 0.12f)
                                ScanType.IMAGE -> Color(0xFFC084FC).copy(alpha = 0.12f)
                                ScanType.AUDIO -> Color(0xFF34D399).copy(alpha = 0.12f)
                                ScanType.VIDEO -> Color(0xFFFBBF24).copy(alpha = 0.12f)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getModalityIcon(type),
                        contentDescription = type.name,
                        tint = when (type) {
                            ScanType.TEXT -> ElectricCyan
                            ScanType.IMAGE -> Color(0xFFC084FC)
                            ScanType.AUDIO -> Color(0xFF34D399)
                            ScanType.VIDEO -> Color(0xFFFBBF24)
                        },
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = title,
                            color = SoftWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "Confidence: ${score.toInt()}%",
                        color = TextGray,
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = dateString,
                        color = TextGray.copy(alpha = 0.6f),
                        fontSize = 9.sp
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                VerdictBadge(isAi = isAi)
                Spacer(modifier = Modifier.height(8.dp))
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove Record",
                        tint = TextGray.copy(alpha = 0.5f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
