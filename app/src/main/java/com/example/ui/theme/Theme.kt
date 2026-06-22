package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val SpaceColorScheme = darkColorScheme(
    primary = ElectricCyan,
    onPrimary = SpaceBackground,
    secondary = GlowPurple,
    onSecondary = SpaceBackground,
    tertiary = ElectricBlue,
    background = SpaceBackground,
    surface = CardSlate,
    onBackground = SoftWhite,
    onSurface = SoftWhite,
    error = AlertRed
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force Dark space theme by default
    dynamicColor: Boolean = false, // Disable dynamic colors to preserve brand style
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) SpaceColorScheme else SpaceColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
