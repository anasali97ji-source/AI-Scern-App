package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ScanType
import com.example.ui.components.*
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DashboardScreen(
    viewModel: ScanViewModel
) {
    val historyItems by viewModel.historyState.collectAsState()
    val dateFormat = SimpleDateFormat("HH:mm • dd MMM", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().height(64.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "AISCERN",
                color = ElectricCyan,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 2.sp
            )
            BadgedBox(
                badge = { Badge(containerColor = ElectricCyan) }
            ) {
                Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = SoftWhite)
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Credit Bar
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
            Text("45/50 scans remaining", color = TextGray, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { 45f / 50f },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = ElectricCyan,
                trackColor = CardSlate
            )
        }
        
        // Bento Grid
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ScanBentoCard(
                title = "Text",
                subtitle = "Paste or upload",
                icon = Icons.Default.Article,
                onClick = { /* navigate handled by tabs usually or main router */ },
                modifier = Modifier.weight(1f)
            )
            ScanBentoCard(
                title = "Image",
                subtitle = "Camera or gallery",
                icon = Icons.Default.Image,
                onClick = { /* navigate */ },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ScanBentoCard(
                title = "Audio",
                subtitle = "Record or file",
                icon = Icons.Default.Mic,
                onClick = { /* navigate */ },
                modifier = Modifier.weight(1f)
            )
            ScanBentoCard(
                title = "Video",
                subtitle = "Upload clip",
                icon = Icons.Default.Videocam,
                onClick = { /* navigate */ },
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Quick Actions Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuickActionItem(title = "Batch Scan", icon = Icons.Default.Layers)
            QuickActionItem(title = "Web Scanner", icon = Icons.Default.Language)
            QuickActionItem(title = "ARIA Chat", icon = Icons.Default.ChatBubble)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Recent Scans Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Recent Activity", color = SoftWhite, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Text("View All →", color = ElectricCyan, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        if (historyItems.isEmpty()) {
            EmptyState(message = "No scans yet\nStart your first detection")
        } else {
            val recentScans = historyItems.sortedByDescending { it.timestamp }.take(5)
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(recentScans) { item ->
                    Box(modifier = Modifier.width(280.dp)) {
                        ResultRow(
                            type = item.type,
                            score = item.score,
                            isAi = item.isAi,
                            title = when (item.type) {
                                ScanType.TEXT -> "Text Analysis"
                                ScanType.IMAGE -> "Image Intel"
                                ScanType.AUDIO -> "Voice Integrity"
                                ScanType.VIDEO -> "Visual Capture"
                            },
                            dateString = dateFormat.format(Date(item.timestamp)),
                            onDelete = { viewModel.deleteItem(item.id) },
                            onClick = { /* Expand view */ }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun ScanBentoCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick).height(120.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF111827)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = ElectricCyan, modifier = Modifier.size(32.dp))
            Column {
                Text(title, color = SoftWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(subtitle, color = TextGray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun QuickActionItem(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.size(48.dp).background(CardSlate, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = ElectricCyan)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(title, color = TextGray, fontSize = 12.sp)
    }
}
