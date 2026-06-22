package com.aiscern.app.ui.screens.detect.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiscern.app.R
import com.aiscern.app.theme.BackgroundSecondary
import com.aiscern.app.theme.BorderSubtle
import com.aiscern.app.theme.TextMuted
import com.aiscern.app.theme.TextPrimary
import com.aiscern.app.theme.AiscernTypography
import com.aiscern.app.ui.components.AiscernPrimaryButton

@Composable
fun VideoPickerPanel(
    onAnalyze: () -> Unit,
    modifier: Modifier = Modifier
) {
    var frameInterval by remember { mutableFloatStateOf(2f) }

    Column(
        modifier = modifier.padding(horizontal = 24.dp)
    ) {
        // Upload zone
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(BackgroundSecondary)
                .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                .clickable { /* Open video picker */ },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.PlayArrow, // replaced Film
                    contentDescription = "Video",
                    tint = TextMuted,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.detect_video_upload),
                    style = AiscernTypography.bodyLarge,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.detect_video_formats),
                    style = AiscernTypography.bodyMedium,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Frame interval slider
        Text(
            text = stringResource(R.string.detect_video_frame_interval),
            style = AiscernTypography.bodyMedium,
            color = TextPrimary
        )
        Slider(
            value = frameInterval,
            onValueChange = { frameInterval = it },
            valueRange = 1f..5f,
            steps = 3,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "${frameInterval.toInt()} seconds",
            style = AiscernTypography.labelMedium,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(16.dp))

        AiscernPrimaryButton(
            text = stringResource(R.string.detect_analyze),
            onClick = onAnalyze,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
