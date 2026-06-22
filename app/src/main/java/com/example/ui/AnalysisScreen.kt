package com.example.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.SpaceBackground
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextGray
import kotlinx.coroutines.delay

@Composable
fun AnalysisScreen(
    onAnalysisComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var progress by remember { mutableFloatStateOf(0f) }
    var statusText by remember { mutableStateOf("Initializing ensemble...") }
    val models = listOf("RoBERTa", "ViT", "wav2vec2", "X-CLIP", "DistilBERT", "ResNet-50", "BERT-Large", "GPT-2 Detector")
    var completedModels by remember { mutableStateOf(emptyList<String>()) }
    
    // Animation for pulsing circles
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale1 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ), label = "scale1"
    )
    val alpha1 by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ), label = "alpha1"
    )

    LaunchedEffect(Unit) {
        val statuses = listOf(
            "Initializing ensemble...",
            "Extracting signals...",
            "Running cross-validation...",
            "Finalizing report..."
        )
        val totalTime = 4000L
        val updateInterval = 50L
        val steps = (totalTime / updateInterval).toInt()
        
        for (i in 1..steps) {
            delay(updateInterval)
            progress = i.toFloat() / steps
            
            // Update status text
            val statusIndex = (progress * statuses.size).toInt().coerceAtMost(statuses.size - 1)
            statusText = statuses[statusIndex]
            
            // Update completed models
            val modelsToComplete = (progress * models.size).toInt().coerceAtMost(models.size)
            completedModels = models.take(modelsToComplete)
        }
        
        onAnalysisComplete()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF070B14)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            // Pulsing animation
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(120.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .scale(scale1)
                        .clip(CircleShape)
                        .background(ElectricCyan.copy(alpha = alpha1))
                )
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(ElectricCyan.copy(alpha = 0.2f))
                )
                Icon(
                    imageVector = Icons.Outlined.Circle,
                    contentDescription = null,
                    tint = ElectricCyan,
                    modifier = Modifier.size(40.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Text(
                text = "${(progress * 100).toInt()}%",
                color = ElectricCyan,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = ElectricCyan,
                trackColor = Color.White.copy(alpha = 0.1f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = statusText,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Models list
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                models.forEach { model ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val isCompleted = completedModels.contains(model)
                        Icon(
                            imageVector = if (isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                            contentDescription = null,
                            tint = if (isCompleted) SuccessGreen else TextGray.copy(alpha = 0.5f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = model,
                            color = if (isCompleted) Color.White else TextGray,
                            fontSize = 12.sp,
                            fontWeight = if (isCompleted) FontWeight.Medium else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}
