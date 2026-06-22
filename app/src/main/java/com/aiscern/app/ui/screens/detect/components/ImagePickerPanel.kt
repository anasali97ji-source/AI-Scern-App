package com.aiscern.app.ui.screens.detect.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.aiscern.app.R
import com.aiscern.app.theme.BackgroundSecondary
import com.aiscern.app.theme.BorderSubtle
import com.aiscern.app.theme.TextMuted
import com.aiscern.app.theme.TextPrimary
import com.aiscern.app.theme.TextSecondary
import com.aiscern.app.theme.AiscernTypography
import com.aiscern.app.ui.components.AiscernPrimaryButton

@Composable
fun ImagePickerPanel(
    selectedUri: Uri?,
    onImageSelected: (Uri) -> Unit,
    onImageCleared: () -> Unit,
    onAnalyze: () -> Unit,
    isDeepScan: Boolean,
    onDeepScanToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp)
    ) {
        if (selectedUri == null) {
            // Upload zone
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(BackgroundSecondary)
                    .border(
                        width = 1.dp,
                        color = BorderSubtle,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { /* Open picker */ },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp, // replaced Upload
                        contentDescription = "Upload",
                        tint = TextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.detect_image_upload),
                        style = AiscernTypography.bodyLarge,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(R.string.detect_image_formats),
                        style = AiscernTypography.bodyMedium,
                        color = TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            // Selected image preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                AsyncImage(
                    model = selectedUri,
                    contentDescription = "Selected image",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = onImageCleared,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove",
                        tint = TextPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Deep scan toggle
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.detect_image_deep_scan),
                style = AiscernTypography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = isDeepScan,
                onCheckedChange = onDeepScanToggle
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AiscernPrimaryButton(
            text = stringResource(R.string.detect_analyze),
            onClick = onAnalyze,
            enabled = selectedUri != null,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
