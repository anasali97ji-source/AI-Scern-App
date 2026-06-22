package com.aiscern.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.aiscern.app.theme.AccentGradient
import com.aiscern.app.theme.BackgroundTertiary
import com.aiscern.app.theme.BorderGlow
import com.aiscern.app.theme.BorderSubtle

@Composable
fun AiscernCard(
    modifier: Modifier = Modifier,
    showGradientBorder: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .background(
                color = BackgroundTertiary,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = if (showGradientBorder) 2.dp else 1.dp,
                brush = if (showGradientBorder) AccentGradient else Brush.linearGradient(
                    listOf(BorderSubtle, BorderSubtle)
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = BorderGlow
            )
            .padding(16.dp)
    ) {
        content()
    }
}
