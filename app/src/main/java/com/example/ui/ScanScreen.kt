package com.example.ui

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.ScanResult
import com.example.data.ScanType
import com.example.ui.components.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun ScanScreen(
    viewModel: ScanViewModel,
    onNavigateToDetail: (ScanResult, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scanUiState by viewModel.scanUiState.collectAsState()
    var selectedType by remember { mutableStateOf(ScanType.TEXT) }
    var textInput by remember { mutableStateOf("") }
    
    var attachedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var attachedFileName by remember { mutableStateOf("") }
    var attachedUriString by remember { mutableStateOf<String?>(null) }
    
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                attachedUriString = uri.toString()
                attachedFileName = uri.lastPathSegment ?: "selected_media"
            }
        }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // High Density Top Navigation & Info Node Header
        TopBrandHeader()

        Spacer(modifier = Modifier.height(10.dp))

        // System Wide Stable Status Card
        SystemStatusCard()

        Spacer(modifier = Modifier.height(20.dp))

        // High Density Detection Modes Grid (2x2)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DetectionModeCard(
                label = "Text Scan",
                sublabel = "NLP Deep Analysis",
                icon = Icons.Default.TextFields,
                tintColor = Color(0xFF00D4FF),
                isSelected = selectedType == ScanType.TEXT,
                onClick = {
                    selectedType = ScanType.TEXT
                    textInput = ""
                    attachedBitmap = null
                    attachedFileName = ""
                    attachedUriString = null
                },
                modifier = Modifier.weight(1f).testTag("tab_text")
            )
            DetectionModeCard(
                label = "Image Lab",
                sublabel = "Pixel Verification",
                icon = Icons.Default.Image,
                tintColor = Color(0xFFC084FC),
                isSelected = selectedType == ScanType.IMAGE,
                onClick = {
                    selectedType = ScanType.IMAGE
                    textInput = ""
                    attachedBitmap = null
                    attachedFileName = ""
                    attachedUriString = null
                },
                modifier = Modifier.weight(1f).testTag("tab_image")
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DetectionModeCard(
                label = "Audio Auth",
                sublabel = "Spectrogram Scan",
                icon = Icons.Default.Mic,
                tintColor = Color(0xFF34D399),
                isSelected = selectedType == ScanType.AUDIO,
                onClick = {
                    selectedType = ScanType.AUDIO
                    textInput = ""
                    attachedBitmap = null
                    attachedFileName = ""
                    attachedUriString = null
                },
                modifier = Modifier.weight(1f).testTag("tab_audio")
            )
            DetectionModeCard(
                label = "Deepfake",
                sublabel = "Temporal Sync Check",
                icon = Icons.Default.Videocam,
                tintColor = Color(0xFFFBBF24),
                isSelected = selectedType == ScanType.VIDEO,
                onClick = {
                    selectedType = ScanType.VIDEO
                    textInput = ""
                    attachedBitmap = null
                    attachedFileName = ""
                    attachedUriString = null
                },
                modifier = Modifier.weight(1f).testTag("tab_video")
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // State Machine content display
        when (val state = scanUiState) {
            ScanUiState.Idle -> {
                // UI inputs based on Type selection
                CyberCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "INPUT SPECIFICATION",
                            color = ElectricCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        when (selectedType) {
                            ScanType.TEXT -> {
                                OutlinedTextField(
                                    value = textInput,
                                    onValueChange = { if (it.length <= 5000) textInput = it },
                                    placeholder = { Text("Paste text content here (max 5000 chars)...", color = TextGray) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(160.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = SoftWhite,
                                        unfocusedTextColor = SoftWhite,
                                        focusedContainerColor = SpaceBackground.copy(alpha = 0.5f),
                                        unfocusedContainerColor = SpaceBackground.copy(alpha = 0.5f),
                                        focusedBorderColor = ElectricCyan,
                                        unfocusedBorderColor = TextGray.copy(alpha = 0.3f)
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    supportingText = {
                                        Text(text = "${textInput.length}/5000", color = TextGray, fontSize = 10.sp, textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth())
                                    }
                                )

                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            val clipboardText = clipboardManager.getText()?.text
                                            if (!clipboardText.isNullOrEmpty()) {
                                                textInput = clipboardText.take(5000)
                                            }
                                        },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = SpaceBackground),
                                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                                    ) {
                                        Icon(Icons.Default.ContentPaste, contentDescription = "Paste", tint = ElectricCyan, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Paste Clipboard", fontSize = 11.sp, color = SoftWhite)
                                    }
                                    Button(
                                        onClick = { filePickerLauncher.launch("application/*") },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = SpaceBackground),
                                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                                    ) {
                                        Icon(Icons.Default.UploadFile, contentDescription = "Upload", tint = GlowPurple, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Upload File", fontSize = 11.sp, color = SoftWhite)
                                    }
                                }

                                if (attachedFileName.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Attached: $attachedFileName", color = GlowPurple, fontSize = 11.sp)
                                }
                            }
                            ScanType.IMAGE -> {
                                val hasImage = attachedUriString != null || attachedFileName.isNotBlank()
                                if (!hasImage) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(SpaceBackground)
                                            .clickable { filePickerLauncher.launch("image/*") },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        androidx.compose.foundation.Canvas(modifier = Modifier.matchParentSize()) {
                                            drawRoundRect(
                                                color = ElectricCyan,
                                                style = Stroke(
                                                    width = 2.dp.toPx(),
                                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                                                ),
                                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx())
                                            )
                                        }
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(Icons.Default.AddPhotoAlternate, contentDescription = "Drop Image", tint = ElectricCyan, modifier = Modifier.size(48.dp))
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text("Tap to select or drop image here", color = TextGray, fontSize = 12.sp)
                                            Text("JPG, PNG, WEBP up to 10MB", color = TextGray.copy(alpha = 0.6f), fontSize = 10.sp)
                                        }
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(SpaceBackground)
                                    ) {
                                        if (attachedUriString != null) {
                                            AsyncImage(
                                                model = attachedUriString,
                                                contentDescription = "Preview",
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                        } else {
                                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                                Icon(Icons.Default.Image, contentDescription = "Mock Image", tint = TextGray, modifier = Modifier.size(64.dp))
                                                Text("Mocked: $attachedFileName", color = Color.White, modifier = Modifier.align(Alignment.BottomCenter).padding(8.dp))
                                            }
                                        }
                                        IconButton(
                                            onClick = { attachedUriString = null; attachedFileName = "" },
                                            modifier = Modifier.align(Alignment.TopEnd).padding(8.dp).background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                                        ) {
                                            Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.White)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            attachedFileName = "CAM_CAPTURE.jpg"
                                            attachedUriString = null
                                        },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = SpaceBackground),
                                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                                    ) {
                                        Icon(Icons.Default.CameraAlt, contentDescription = "Camera", tint = SoftWhite, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Camera", fontSize = 11.sp, color = SoftWhite)
                                    }
                                    Button(
                                        onClick = { filePickerLauncher.launch("image/*") },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = SpaceBackground),
                                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                                    ) {
                                        Icon(Icons.Default.PhotoLibrary, contentDescription = "Gallery", tint = SoftWhite, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Gallery", fontSize = 11.sp, color = SoftWhite)
                                    }
                                }
                            }
                            ScanType.AUDIO -> {
                                var isRecording by remember { mutableStateOf(false) }
                                var recordTime by remember { mutableStateOf(0) }

                                LaunchedEffect(isRecording) {
                                    while (isRecording && recordTime < 300) { // Max 5 mins
                                        delay(1000)
                                        recordTime++
                                    }
                                    if (recordTime >= 300) isRecording = false
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(SpaceBackground)
                                        .padding(20.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = String.format(Locale.US, "%02d:%02d", recordTime / 60, recordTime % 60),
                                            color = if (isRecording) AlertRed else SoftWhite,
                                            fontSize = 32.sp,
                                            fontWeight = FontWeight.Light,
                                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        // Mock waveform visualization
                                        Row(
                                            modifier = Modifier.height(40.dp).fillMaxWidth(0.6f),
                                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            repeat(15) { index ->
                                                val height = if (isRecording) (10..40).random() else 2
                                                Box(modifier = Modifier.weight(1f).height(height.dp).background(if (isRecording) ElectricCyan else TextGray))
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(24.dp))
                                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                            IconButton(
                                                onClick = {
                                                    isRecording = !isRecording
                                                    if (isRecording) {
                                                        attachedFileName = "AudioRecord_${System.currentTimeMillis()}.m4a"
                                                    }
                                                },
                                                modifier = Modifier.size(56.dp).clip(RoundedCornerShape(28.dp)).background(if (isRecording) AlertRed.copy(alpha = 0.2f) else ElectricCyan.copy(alpha = 0.2f))
                                            ) {
                                                Icon(
                                                    imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                                                    contentDescription = "Record",
                                                    tint = if (isRecording) AlertRed else ElectricCyan,
                                                    modifier = Modifier.size(28.dp)
                                                )
                                            }
                                            if (recordTime > 0 && !isRecording) {
                                                IconButton(
                                                    onClick = { /* mock play */ },
                                                    modifier = Modifier.size(56.dp).clip(RoundedCornerShape(28.dp)).background(SuccessGreen.copy(alpha = 0.2f))
                                                ) {
                                                    Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = SuccessGreen, modifier = Modifier.size(28.dp))
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = { filePickerLauncher.launch("audio/*") },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = SpaceBackground),
                                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                                ) {
                                    Icon(Icons.Default.AudioFile, contentDescription = "Import Audio File", tint = GlowPurple, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Import Audio File (.mp3, .wav)", fontSize = 12.sp, color = SoftWhite)
                                }
                                if (attachedFileName.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Ready to analyze: $attachedFileName", color = SuccessGreen, fontSize = 11.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                                }
                            }
                            ScanType.VIDEO -> {
                                val hasVideo = attachedUriString != null || attachedFileName.isNotBlank()
                                
                                if (!hasVideo) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(160.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(SpaceBackground)
                                            .clickable { filePickerLauncher.launch("video/*") },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(Icons.Default.VideoLibrary, contentDescription = "Drop Video", tint = GlowPurple, modifier = Modifier.size(48.dp))
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text("Select Video File to Scan", color = TextGray, fontSize = 12.sp)
                                            Text("MP4, MOV, AVI up to 100MB", color = TextGray.copy(alpha = 0.6f), fontSize = 10.sp)
                                        }
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(160.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color.Black)
                                    ) {
                                        // Mock video thumbnail
                                        Icon(
                                            Icons.Default.PlayCircle, 
                                            contentDescription = "Play", 
                                            tint = Color.White.copy(alpha = 0.8f), 
                                            modifier = Modifier.size(48.dp).align(Alignment.Center)
                                        )
                                        
                                        // Top bar with remove
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.7f), Color.Transparent)))
                                                .padding(8.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Text(
                                                text = attachedFileName,
                                                color = Color.White,
                                                fontSize = 11.sp,
                                                modifier = Modifier.weight(1f)
                                            )
                                            IconButton(
                                                onClick = { attachedUriString = null; attachedFileName = "" },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.White)
                                            }
                                        }
                                        
                                        // Bottom info bar (mock duration/size)
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .align(Alignment.BottomCenter)
                                                .background(Color.Black.copy(alpha = 0.5f))
                                                .padding(8.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("00:42", color = Color.White, fontSize = 10.sp)
                                            Text("14.2 MB", color = Color.White, fontSize = 10.sp)
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // SCAN TRIGGER
                        val isInputValid = textInput.isNotBlank() || attachedFileName.isNotBlank()
                        AnimatedButton(
                            text = if (isInputValid) "ANALYZE" else "PROVIDE VALID INPUT",
                            onClick = {
                                viewModel.triggerScan(selectedType, textInput, attachedBitmap)
                            },
                            enabled = isInputValid,
                            modifier = Modifier.testTag("scan_submit_btn")
                        )
                    }
                }
            }
            ScanUiState.Scanning -> {
                AnalysisScreen(
                    onAnalysisComplete = {
                        // The actual scan was triggered before we entered this state, 
                        // so the ViewModel is likely already in Success state. 
                        // But wait! If the viewmodel finishes before the animation, we need to defer showing results.
                        // Actually, it's better to let `AnalysisScreen` be just the visual. But how to deal with state?
                    }
                )
            }
            is ScanUiState.Success -> {
                val result = state.result
                val rawInput = state.rawInput

                LaunchedEffect(Unit) {
                    onNavigateToDetail(result, rawInput)
                    viewModel.resetState()
                }
            }
            is ScanUiState.Error -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = AlertRed.copy(alpha = 0.15f)),
                    border = BorderStroke(1.dp, AlertRed.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Error, "Error", tint = AlertRed, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "SPECTRUM ERROR",
                            color = AlertRed,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = state.message,
                            color = SoftWhite,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        AnimatedButton(
                            text = "RESET SCANNER",
                            onClick = { viewModel.resetState() }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun DetectionModeCard(
    label: String,
    sublabel: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
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
