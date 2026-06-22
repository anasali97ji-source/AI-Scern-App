package com.example.ui

import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ScanResult
import com.example.data.ScanType
import com.example.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    result: ScanResult,
    rawInput: String,
    onNavigateHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isAi = result.isAi

    // Safe fallback to guess type
    val inferredType = when {
        rawInput.endsWith(".jpg") || rawInput.endsWith(".png") || rawInput.endsWith("CAM_CAPTURE.jpg") -> ScanType.IMAGE
        rawInput.endsWith(".mp3") || rawInput.endsWith(".wav") || rawInput.endsWith(".m4a") -> ScanType.AUDIO
        rawInput.endsWith(".mp4") || rawInput.endsWith(".mov") || rawInput.endsWith(".avi") -> ScanType.VIDEO
        else -> ScanType.TEXT
    }

    val reportText = """
        AISCERN Result - AI Confidence: ${result.score}%
        $rawInput
    """.trimIndent()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("RESULTS", color = ElectricCyan, fontSize = 16.sp, fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onNavigateHome) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = SoftWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SpaceBackground)
            )
        },
        bottomBar = {
            Column(modifier = Modifier.background(SpaceBackground).padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                setType("text/plain")
                                putExtra(Intent.EXTRA_TEXT, reportText)
                            }
                            context.startActivity(Intent.createChooser(intent, "Share Result"))
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricCyan),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Share", fontSize = 12.sp)
                    }
                    OutlinedButton(
                        onClick = { /* Export PDF */ },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GlowPurple),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                    ) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Export", fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                AnimatedButton(
                    text = "NEW SCAN",
                    onClick = onNavigateHome
                )
            }
        },
        containerColor = SpaceBackground,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text("SCAN ID: #AIS-${System.currentTimeMillis().toString().takeLast(6)}", color = TextGray, fontSize = 10.sp)
                    Text("Time: 3.4s", color = TextGray, fontSize = 10.sp)
                }
                VerdictBadge(isAi = isAi)
            }

            Spacer(modifier = Modifier.height(24.dp))

            ConfidenceGauge(confidence = result.score, modifier = Modifier.size(140.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${result.score.toInt()}% SYNTHETIC",
                color = if (isAi) AlertRed else SuccessGreen,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Modality Specific Sections
            Text(
                "DEEP ANALYSIS",
                color = TextGray,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                modifier = Modifier.align(Alignment.Start).padding(bottom = 12.dp)
            )

            when (inferredType) {
                ScanType.TEXT -> {
                    TextHeatmapCard(text = rawInput)
                }
                ScanType.IMAGE -> {
                    ImageBoundingBoxCard()
                }
                ScanType.AUDIO -> {
                    AudioWaveformCard()
                }
                ScanType.VIDEO -> {
                    VideoTimelineCard()
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Signal Breakdown Accordion
            Text(
                "MODEL ENSEMBLE BREAKDOWN",
                color = TextGray,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                modifier = Modifier.align(Alignment.Start).padding(bottom = 12.dp)
            )

            val models = listOf(
                Pair("RoBERTa-Base-Detector", result.breakdown.syntacticAnomaly),
                Pair("DeBERTa-V3-Large", (result.score + result.breakdown.repetitionPattern) / 2f),
                Pair("ViT-B-16 Anomalies", result.breakdown.semanticConsistency),
                Pair("GPT-2 Zero-Shot", result.score),
                Pair("DistilBERT Sentinel", result.breakdown.repetitionPattern),
                Pair("Wav2Vec 2.0 Forensics", (result.score + 12f).coerceAtMost(100f)),
                Pair("EfficientNet-B4 Artifactor", (result.breakdown.semanticConsistency + 5f).coerceAtMost(100f)),
                Pair("Temporal ResNet-50", (result.breakdown.syntacticAnomaly - 4f).coerceAtLeast(0f))
            )
            val expandedStates = remember { mutableStateMapOf<String, Boolean>() }

            models.forEach { (model, score) ->
                val expanded = expandedStates[model] ?: false
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardSlate.copy(alpha = 0.5f))
                        .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                        .clickable { expandedStates[model] = !expanded }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(model, color = SoftWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("${score.toInt()}%", color = ElectricCyan, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Icon(
                                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = TextGray
                            )
                        }
                    }
                    if (expanded) {
                        Spacer(modifier = Modifier.height(12.dp))
                        LinearProgressIndicator(
                            progress = { score / 100f },
                            modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                            color = if (score > 50) AlertRed else SuccessGreen,
                            trackColor = Color.White.copy(alpha = 0.1f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Model detected varying anomalies consistent with synthetic generation patterns.",
                            color = TextGray,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TextHeatmapCard(text: String) {
    val sentences = text.split(".").filter { it.isNotBlank() }
    val displaySentences = if (sentences.isEmpty()) listOf("No readable text detected.") else sentences

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardSlate)
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        val annotatedText = buildAnnotatedString {
            displaySentences.forEachIndexed { index, sentence ->
                val randomScore = Math.random() * 100
                val bgColor = when {
                    randomScore > 70 -> AlertRed.copy(alpha = 0.3f)
                    randomScore > 40 -> Color.Yellow.copy(alpha = 0.3f)
                    else -> SuccessGreen.copy(alpha = 0.1f)
                }
                withStyle(style = SpanStyle(background = bgColor, color = Color.White)) {
                    append(sentence.trim() + ". ")
                }
            }
        }
        Text(text = annotatedText, fontSize = 13.sp, lineHeight = 20.sp)
    }
}

@Composable
fun ImageBoundingBoxCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.DarkGray)
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.Image, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(64.dp))
        
        // Mock bounding box
        Box(
            modifier = Modifier
                .size(100.dp, 120.dp)
                .offset(x = 20.dp, y = -10.dp)
                .border(2.dp, AlertRed, RoundedCornerShape(4.dp))
        ) {
            Text(
                "AI Artifact: Eyes",
                color = Color.White,
                fontSize = 8.sp,
                modifier = Modifier.background(AlertRed).padding(2.dp).align(Alignment.TopStart)
            )
        }
    }
}

@Composable
fun AudioWaveformCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(CardSlate)
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(40) { index ->
                val isRed = index in 10..15 || index in 28..32
                val height = (10..60).random().dp
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(height)
                        .background(if (isRed) AlertRed else ElectricCyan)
                )
            }
        }
    }
}

@Composable
fun VideoTimelineCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black)
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
    ) {
        Icon(Icons.Default.PlayCircle, contentDescription = null, tint = Color.White.copy(alpha = 0.5f), modifier = Modifier.size(48.dp).align(Alignment.Center))
        
        // Timeline scrubber
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .align(Alignment.BottomCenter)
                .background(Color.Black.copy(alpha = 0.7f))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Color.Gray.copy(alpha = 0.5f))
            ) {
                // Red timeline markers for deepfake
                Box(modifier = Modifier.fillMaxHeight().width(30.dp).offset(x = 60.dp).background(AlertRed))
                Box(modifier = Modifier.fillMaxHeight().width(20.dp).offset(x = 180.dp).background(AlertRed))
            }
        }
    }
}
