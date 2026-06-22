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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiscern.app.R
import com.aiscern.app.theme.AccentViolet
import com.aiscern.app.theme.BackgroundSecondary
import com.aiscern.app.theme.BorderSubtle
import com.aiscern.app.theme.TextInverse
import com.aiscern.app.theme.TextMuted
import com.aiscern.app.theme.TextSecondary
import com.aiscern.app.theme.AiscernTypography
import com.aiscern.app.ui.components.AiscernPrimaryButton

@Composable
fun AudioRecorderPanel(
    onAnalyze: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Waveform placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(BackgroundSecondary)
                .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Waveform visualization",
                style = AiscernTypography.bodyMedium,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Record button
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(AccentViolet)
                .clickable { /* Start recording */ },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = "Record",
                tint = TextInverse,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.detect_audio_hint),
            style = AiscernTypography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Upload option
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(BackgroundSecondary)
                .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                .clickable { /* Open file picker */ },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp, // replaced Upload
                    contentDescription = "Upload",
                    tint = TextMuted,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.detect_audio_formats),
                    style = AiscernTypography.bodyMedium,
                    color = TextMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        AiscernPrimaryButton(
            text = stringResource(R.string.detect_analyze),
            onClick = onAnalyze,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
