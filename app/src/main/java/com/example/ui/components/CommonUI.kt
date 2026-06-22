package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import com.example.ui.theme.*

// Predefined cyber gradient styles
val SpaceHeroGradient = Brush.linearGradient(
    colors = listOf(ElectricCyan, ElectricBlue, GlowPurple),
    start = Offset(0f, 0f),
    end = Offset(1000f, 1000f)
)

val DarkCyberGradient = Brush.verticalGradient(
    colors = listOf(SpaceBackground, Color(0xFF1E1E2F))
)

val RedAiGradient = Brush.linearGradient(
    colors = listOf(Color(0xFFEF4444), Color(0xFFF97316), Color(0xFFB91C1C))
)

val GreenHumanGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF10B981), Color(0xFF059669), Color(0xFF047857))
)

@Composable
fun CyberCard(
    modifier: Modifier = Modifier,
    borderColor: Color = ElectricCyan.copy(alpha = 0.3f),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = CardSlate.copy(alpha = 0.7f)),
        shape = RoundedCornerShape(16.dp),
        content = content
    )
}

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    brush: Brush = SpaceHeroGradient,
    textColor: Color = SpaceBackground,
    enabled: Boolean = true
) {
    val bgModifier = if (enabled) {
        Modifier.background(brush)
    } else {
        Modifier.background(TextGray.copy(alpha = 0.3f))
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .then(bgModifier)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (enabled) textColor else TextGray,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun TopBrandHeader(
    modifier: Modifier = Modifier,
    onNotificationClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.aiscern_logo_new_1782031452562),
                contentDescription = "AISCERN Logo",
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "AISCERN",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        brush = SpaceHeroGradient,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp,
                        fontSize = 26.sp
                    )
                )
                Text(
                    text = "MULTI-MODAL DETECTION",
                    color = TextGray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Notifications Icon Button with glowing border
            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardSlate)
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = ElectricCyan,
                    modifier = Modifier.size(20.dp)
                )
            }

            // User Profile circle active representation
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(ElectricCyan),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User Node Context",
                    tint = SpaceBackground,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun SystemStatusCard(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_engine")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    val progressValue = 0.98f

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF2563EB).copy(alpha = 0.15f),
                        Color(0xFF9333EA).copy(alpha = 0.15f)
                    )
                )
            )
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(28.dp))
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "SYSTEM STATUS",
                    color = Color(0xFF93C5FD), // text-blue-300
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Neural Engines\nOnline",
                    color = SoftWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .drawBehind {
                                drawCircle(
                                    color = ElectricCyan.copy(alpha = pulseAlpha),
                                    radius = size.minDimension / 2f + 4.dp.toPx() * (1f - pulseAlpha)
                                )
                                drawCircle(
                                    color = ElectricCyan,
                                    radius = size.minDimension / 2f
                                )
                            }
                    )
                    Text(
                        text = "v5.2.0-STABLE",
                        color = ElectricCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }

            // Radial Uptime Gauge
            Box(
                modifier = Modifier.size(76.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Outer track
                    drawCircle(
                        color = Color.White.copy(alpha = 0.05f),
                        radius = size.minDimension / 2f - 4.dp.toPx(),
                        style = Stroke(width = 4.dp.toPx())
                    )
                    // High Density filled track
                    drawArc(
                        color = ElectricCyan,
                        startAngle = -90f,
                        sweepAngle = progressValue * 360f,
                        useCenter = false,
                        topLeft = Offset(4.dp.toPx(), 4.dp.toPx()),
                        size = androidx.compose.ui.geometry.Size(size.width - 8.dp.toPx(), size.height - 8.dp.toPx()),
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "98%",
                        color = SoftWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "UPTIME",
                        color = TextGray,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CyberGauge(
    score: Float, // 0 to 100
    label: String,
    modifier: Modifier = Modifier,
    size: Dp = 140.dp,
    strokeWidth: Dp = 12.dp,
    isAiFlag: Boolean = false
) {
    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(score) {
        animatedProgress.animateTo(
            targetValue = score / 100f,
            animationSpec = spring(dampingRatio = 0.6f, stiffness = 150f)
        )
    }

    val activeColor = if (isAiFlag) AlertRed else SuccessGreen
    val glowColor = activeColor.copy(alpha = 0.25f)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(size),
            contentAlignment = Alignment.Center
        ) {
            // Draw background track and progress arc on Canvas for maximum performance
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Background Track
                drawCircle(
                    color = Color.White.copy(alpha = 0.05f),
                    radius = (size / 2).toPx() - strokeWidth.toPx(),
                    style = Stroke(width = strokeWidth.toPx())
                )

                val pad1 = strokeWidth.toPx()
                val pad2 = strokeWidth.toPx() * 2

                // Glowing Outer Halo
                drawArc(
                    color = glowColor,
                    startAngle = -90f,
                    sweepAngle = animatedProgress.value * 360f,
                    useCenter = false,
                    topLeft = Offset(pad1 / 2, pad1 / 2),
                    size = androidx.compose.ui.geometry.Size(this.size.width - pad1, this.size.height - pad1),
                    style = Stroke(width = pad1 * 1.5f, cap = StrokeCap.Round)
                )

                // Active Core Arc
                drawArc(
                    color = activeColor,
                    startAngle = -90f,
                    sweepAngle = animatedProgress.value * 360f,
                    useCenter = false,
                    topLeft = Offset(pad1, pad1),
                    size = androidx.compose.ui.geometry.Size(this.size.width - pad2, this.size.height - pad2),
                    style = Stroke(width = pad1, cap = StrokeCap.Round)
                )
            }

            // Central percentage value
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${(animatedProgress.value * 100).toInt()}%",
                    color = SoftWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isAiFlag) "AI PROB" else "AUTHENTIC",
                    color = TextGray,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = label,
            color = SoftWhite,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun HolographicScanner(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scanner")
    
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val beamTranslate by infiniteTransition.animateFloat(
        initialValue = -50f,
        targetValue = 350f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "beam"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .border(2.dp, ElectricCyan.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
            .background(CardSlate.copy(alpha = 0.4f), RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
    ) {
        // Glowing pulses behind
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = ElectricCyan.copy(alpha = 0.12f * (2f - pulseScale)),
                radius = 120.dp.toPx() * pulseScale,
                center = center
            )
            drawCircle(
                color = GlowPurple.copy(alpha = 0.08f),
                radius = 70.dp.toPx() * (2.1f - pulseScale),
                center = center
            )
            
            // Crosshairs
            drawLine(
                color = ElectricCyan.copy(alpha = 0.25f),
                start = Offset(center.x - 140.dp.toPx(), center.y),
                end = Offset(center.x + 140.dp.toPx(), center.y),
                strokeWidth = 2f
            )
            drawLine(
                color = ElectricCyan.copy(alpha = 0.25f),
                start = Offset(center.x, center.y - 100.dp.toPx()),
                end = Offset(center.x, center.y + 100.dp.toPx()),
                strokeWidth = 2f
            )
            
            // Reticle Circles
            drawCircle(
                color = ElectricCyan.copy(alpha = 0.3f),
                radius = 45.dp.toPx(),
                center = center,
                style = Stroke(width = 2.dp.toPx())
            )
            drawCircle(
                color = ElectricCyan.copy(alpha = 0.15f),
                radius = 90.dp.toPx(),
                center = center,
                style = Stroke(width = 1.dp.toPx())
            )

            // Scanning horizontal line beam
            drawLine(
                color = ElectricCyan,
                start = Offset(40.dp.toPx(), beamTranslate),
                end = Offset(size.width - 40.dp.toPx(), beamTranslate),
                strokeWidth = 4.dp.toPx()
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .border(2.dp, GlowPurple.copy(alpha = 0.6f), RoundedCornerShape(50.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "CERN",
                    color = ElectricCyan,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "NEURAL SPECTRUM ANALYSIS...",
                color = SoftWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )
            Text(
                text = "Deconstructive frequency decomposition active",
                color = TextGray,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
