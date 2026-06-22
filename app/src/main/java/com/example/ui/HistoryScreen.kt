package com.example.ui

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HistoryEntity
import com.example.data.HistoryMapper
import com.example.data.ScanResult
import com.example.data.ScanType
import com.example.ui.components.*
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

enum class HistoryFilter { ALL, TEXT, IMAGE, AUDIO, VIDEO, AI_ONLY, HUMAN_ONLY }

data class HistoryExportItem(
    val id: Long,
    val type: String,
    val isAi: Boolean,
    val score: Float,
    val inputDescription: String,
    val timestamp: Long
)

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: ScanViewModel,
    onNavigateToDetail: (ScanResult, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val historyList by viewModel.historyState.collectAsState()
    val context = LocalContext.current
    
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(HistoryFilter.ALL) }
    var itemsToShow by remember { mutableIntStateOf(20) }

    val filteredList = remember(historyList, searchQuery, selectedFilter) {
        historyList
            .filter { item ->
                if (searchQuery.isBlank()) true else item.inputTextOrPath.contains(searchQuery, ignoreCase = true)
            }
            .filter { item ->
                when (selectedFilter) {
                    HistoryFilter.ALL -> true
                    HistoryFilter.TEXT -> item.type == ScanType.TEXT
                    HistoryFilter.IMAGE -> item.type == ScanType.IMAGE
                    HistoryFilter.AUDIO -> item.type == ScanType.AUDIO
                    HistoryFilter.VIDEO -> item.type == ScanType.VIDEO
                    HistoryFilter.AI_ONLY -> item.isAi
                    HistoryFilter.HUMAN_ONLY -> !item.isAi
                }
            }
    }

    val displayList = filteredList.take(itemsToShow)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App History Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "ACTIVE ARCHIVE",
                    color = TextGray,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
            }

            Row {
                IconButton(
                    onClick = {
                        val exportData = historyList.map {
                            HistoryExportItem(it.id, it.type.name, it.isAi, it.score, it.inputTextOrPath, it.timestamp)
                        }
                        val jsonString = buildString {
                            append("[")
                            exportData.forEachIndexed { index, item ->
                                val escapedInput = item.inputDescription.replace("\"", "\\\"").replace("\n", "\\n")
                                append("""{"id":${item.id},"type":"${item.type}","isAi":${item.isAi},"score":${item.score},"inputDescription":"$escapedInput","timestamp":${item.timestamp}}""")
                                if (index < exportData.size - 1) append(",")
                            }
                            append("]")
                        }
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "application/json"
                            putExtra(Intent.EXTRA_TEXT, jsonString)
                        }
                        context.startActivity(Intent.createChooser(intent, "Export History"))
                    },
                    modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(ElectricCyan.copy(alpha = 0.1f))
                ) {
                    Icon(Icons.Default.Download, contentDescription = "Export JSON", tint = ElectricCyan, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                if (historyList.isNotEmpty()) {
                    IconButton(
                        onClick = { viewModel.clearAllHistory() },
                        modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(AlertRed.copy(alpha = 0.1f))
                    ) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = "Clear History", tint = AlertRed, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search logs...", color = TextGray, fontSize = 12.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextGray, modifier = Modifier.size(18.dp)) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ElectricCyan,
                unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                focusedContainerColor = SpaceBackground.copy(alpha = 0.5f),
                unfocusedContainerColor = SpaceBackground.copy(alpha = 0.5f),
                focusedTextColor = SoftWhite,
                unfocusedTextColor = SoftWhite
            ),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val filters = HistoryFilter.values()
            items(filters) { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter.name.replace("_", " "), fontSize = 11.sp, color = if (selectedFilter == filter) SpaceBackground else TextGray) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ElectricCyan,
                        containerColor = CardSlate
                    ),
                    border = FilterChipDefaults.filterChipBorder(enabled = true, selected = selectedFilter == filter)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // History list representation
        if (displayList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(CardSlate.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.HistoryToggleOff,
                            contentDescription = "No Telemetry",
                            tint = ElectricCyan.copy(alpha = 0.6f),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "NO TELEMETRY MATCHES",
                        color = SoftWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        } else {
            val listState = rememberLazyListState()
            
            // Simple pagination trigger
            LaunchedEffect(listState) {
                snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                    .collect { lastIndex ->
                        if (lastIndex != null && lastIndex >= itemsToShow - 5 && itemsToShow < filteredList.size) {
                            itemsToShow += 20
                        }
                    }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize().testTag("history_list"),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(displayList, key = { it.id }) { item ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { dismissValue ->
                            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                                viewModel.deleteItem(item.id)
                                true
                            } else {
                                false
                            }
                        }
                    )
                    
                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(AlertRed)
                                    .padding(end = 24.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                            }
                        },
                        enableDismissFromStartToEnd = false,
                        modifier = Modifier.animateItem()
                    ) {
                        HistoryItemCard(
                            item = item,
                            onClick = {
                                val scanResult = HistoryMapper.fromEntity(item)
                                onNavigateToDetail(scanResult, item.inputTextOrPath)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItemCard(
    item: HistoryEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormatter = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) }
    val dateString = dateFormatter.format(Date(item.timestamp))

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(CardSlate)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        when (item.type) {
                            ScanType.TEXT -> ElectricCyan.copy(alpha = 0.12f)
                            ScanType.IMAGE -> GlowPurple.copy(alpha = 0.12f)
                            ScanType.AUDIO -> ElectricBlue.copy(alpha = 0.12f)
                            ScanType.VIDEO -> AlertRed.copy(alpha = 0.12f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (item.type) {
                        ScanType.TEXT -> Icons.Default.TextFields
                        ScanType.IMAGE -> Icons.Default.Image
                        ScanType.AUDIO -> Icons.Default.Mic
                        ScanType.VIDEO -> Icons.Default.Videocam
                    },
                    contentDescription = item.type.name,
                    tint = when (item.type) {
                        ScanType.TEXT -> ElectricCyan
                        ScanType.IMAGE -> GlowPurple
                        ScanType.AUDIO -> ElectricBlue
                        ScanType.VIDEO -> AlertRed
                    },
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = when (item.type) {
                            ScanType.TEXT -> "Text"
                            ScanType.IMAGE -> "Image"
                            ScanType.AUDIO -> "Audio"
                            ScanType.VIDEO -> "Video"
                        },
                        color = SoftWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (item.isAi) AlertRed.copy(alpha = 0.15f) else SuccessGreen.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (item.isAi) "AI" else "HUMAN",
                            color = if (item.isAi) AlertRed else SuccessGreen,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${item.score.toInt()}%",
                        color = if (item.score > 50) AlertRed else SuccessGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = item.inputTextOrPath.ifBlank { "Attached media" },
                    color = TextGray,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = dateString,
                    color = TextGray.copy(alpha = 0.6f),
                    fontSize = 9.sp
                )
            }
        }
    }
}

