package com.aiscern.app.ui.screens.detect.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.aiscern.app.R
import com.aiscern.app.theme.ErrorRed
import com.aiscern.app.theme.SuccessGreen
import com.aiscern.app.theme.TextMuted
import com.aiscern.app.theme.WarningAmber
import com.aiscern.app.theme.AiscernTypography
import com.aiscern.app.ui.components.AiscernPrimaryButton
import com.aiscern.app.ui.components.AiscernTextField

@Composable
fun TextInputPanel(
    text: String,
    onTextChange: (String) -> Unit,
    onAnalyze: () -> Unit,
    isAnalyzing: Boolean,
    modifier: Modifier = Modifier
) {
    val charCount = text.length
    val isValid = charCount >= 50
    val counterColor = when {
        charCount >= 5000 -> ErrorRed
        charCount >= 4500 -> WarningAmber
        else -> TextMuted
    }

    Column(
        modifier = modifier.padding(horizontal = 24.dp)
    ) {
        AiscernTextField(
            value = text,
            onValueChange = onTextChange,
            placeholder = stringResource(R.string.detect_text_placeholder),
            maxLines = 10,
            minLines = 6,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { if (isValid) onAnalyze() }),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Character counter
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (!isValid) {
                Text(
                    text = stringResource(R.string.detect_text_min_chars, 50 - charCount),
                    style = AiscernTypography.labelMedium,
                    color = WarningAmber
                )
            } else {
                Text(
                    text = "Ready to analyze",
                    style = AiscernTypography.labelMedium,
                    color = SuccessGreen
                )
            }
            Text(
                text = stringResource(R.string.detect_text_counter, charCount),
                style = AiscernTypography.labelMedium,
                color = counterColor
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AiscernPrimaryButton(
            text = stringResource(R.string.detect_analyze),
            onClick = onAnalyze,
            isLoading = isAnalyzing,
            enabled = isValid && !isAnalyzing,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
