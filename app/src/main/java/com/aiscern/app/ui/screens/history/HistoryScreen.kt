package com.aiscern.app.ui.screens.history

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aiscern.app.R
import com.aiscern.app.theme.AccentViolet
import com.aiscern.app.theme.BackgroundPrimary
import com.aiscern.app.theme.BackgroundSecondary
import com.aiscern.app.theme.ErrorRed
import com.aiscern.app.theme.SuccessGreen
import com.aiscern.app.theme.TextInverse
import com.aiscern.app.theme.TextMuted
import com.aiscern.app.theme.TextPrimary
import com.aiscern.app.theme.TextSecondary
import com.aiscern.app.theme.WarningAmber
import com.aiscern.app.theme.AiscernTypography
import com.aiscern.app.ui.components.AiscernCard
import com.aiscern.app.ui.components.AiscernPrimaryButton
import com.aiscern.app.ui.components.AiscernTextField
import com.aiscern.app.ui.components.AiscernTopAppBar
import java.util.Locale

@Composable
fun HistoryScreen(
    onScanClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    // Mock data
    val historyItems = remember {
        listOf(
            HistoryItem("1", "TEXT", "This is a sample text scan result...", 0.87f, "ai_generated", "2 min ago"),
            HistoryItem("2", "IMAGE", "photo_001.jpg", 0.34f, "human", "1 hour ago"),
            HistoryItem("3", "AUDIO", "recording_005.mp3", 0.92f, "ai_generated", "3 hours ago"),
            HistoryItem("4", "VIDEO", "video_interview.mp4", 0.56f, "uncertain", "Yesterday")
        )
    }

    val filteredItems = historyItems.filter {
        it.preview.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundPrimary)
    ) {
        AiscernTopAppBar(
            title = stringResource(R.string.history_title),
            actions = {
                IconButton(onClick = { isSearchActive = !isSearchActive }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = TextSecondary
                    )
                }
            }
        )

        if (isSearchActive) {
            AiscernTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = stringResource(R.string.history_search_hint),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            )
        }

        if (filteredItems.isEmpty()) {
            EmptyHistoryState(
                onStartScan = { /* Navigate to detect */ }
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredItems, key = { it.id }) { item ->
                    HistoryListItem(
                        item = item,
                        onClick = { onScanClick(item.id) },
                        onDelete = { /* Delete item */ }
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryListItem(
    item: HistoryItem,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else false
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(ErrorRed)
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = TextInverse
                )
            }
        },
        modifier = modifier
    ) {
        AiscernCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Modality icon
                val (icon, iconColor) = when (item.modality) {
                    "TEXT" -> Pair(Icons.AutoMirrored.Filled.List, AccentViolet)
                    "IMAGE" -> Pair(Icons.Default.Image, AccentViolet)
                    "AUDIO" -> Pair(Icons.Default.MusicNote, AccentViolet)
                    "VIDEO" -> Pair(Icons.Default.Videocam, AccentViolet)
                    else -> Pair(Icons.AutoMirrored.Filled.List, TextMuted)
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(BackgroundSecondary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = item.modality,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Content
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = item.preview,
                        style = AiscernTypography.bodyMedium,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${item.timestamp} • ${item.verdict.replace("_", " ").replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}",
                        style = AiscernTypography.labelMedium,
                        color = TextMuted
                    )
                }

                // Confidence
                val confidenceColor = when {
                    item.confidence < 0.3f -> SuccessGreen
                    item.confidence < 0.7f -> WarningAmber
                    else -> ErrorRed
                }

                Text(
                    text = "${(item.confidence * 100).toInt()}%",
                    style = AiscernTypography.labelLarge,
                    color = confidenceColor
                )
            }
        }
    }
}

@Composable
private fun EmptyHistoryState(
    onStartScan: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(60.dp))
                .background(BackgroundSecondary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.history_empty_title),
            style = AiscernTypography.headlineMedium,
            color = TextPrimary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.history_empty_subtitle),
            style = AiscernTypography.bodyLarge,
            color = TextSecondary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        AiscernPrimaryButton(
            text = "Start First Scan",
            onClick = onStartScan,
            modifier = Modifier.fillMaxWidth(0.7f)
        )
    }
}

data class HistoryItem(
    val id: String,
    val modality: String,
    val preview: String,
    val confidence: Float,
    val verdict: String,
    val timestamp: String
)
