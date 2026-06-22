package com.aiscern.app.ui.screens.detect.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aiscern.app.R
import com.aiscern.app.theme.AccentViolet
import com.aiscern.app.theme.BackgroundSecondary
import com.aiscern.app.theme.TextInverse
import com.aiscern.app.theme.TextMuted
import com.aiscern.app.theme.AiscernTypography
import com.aiscern.app.ui.screens.detect.Modality

@Composable
fun ModalitySelector(
    selectedModality: Modality,
    onModalitySelected: (Modality) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        Triple(Modality.TEXT, Icons.AutoMirrored.Filled.List, stringResource(R.string.detect_modality_text)),
        Triple(Modality.IMAGE, Icons.Default.Image, stringResource(R.string.detect_modality_image)),
        Triple(Modality.AUDIO, Icons.Default.MusicNote, stringResource(R.string.detect_modality_audio)),
        Triple(Modality.VIDEO, Icons.Default.Videocam, stringResource(R.string.detect_modality_video))
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BackgroundSecondary)
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items.forEach { (modality, icon, label) ->
            val isSelected = selectedModality == modality
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) AccentViolet else BackgroundSecondary)
                    .clickable { onModalitySelected(modality) }
                    .padding(vertical = 10.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isSelected) TextInverse else TextMuted,
                    modifier = Modifier.size(18.dp)
                )
                if (isSelected) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = label,
                        style = AiscernTypography.labelMedium,
                        color = TextInverse
                    )
                }
            }
        }
    }
}
