package com.aiscern.app.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AccentViolet,
    onPrimary = TextInverse,
    primaryContainer = AccentViolet.copy(alpha = 0.15f),
    onPrimaryContainer = AccentViolet,
    secondary = AccentBlue,
    onSecondary = TextInverse,
    secondaryContainer = AccentBlue.copy(alpha = 0.15f),
    onSecondaryContainer = AccentBlue,
    tertiary = InfoCyan,
    onTertiary = TextInverse,
    background = BackgroundPrimary,
    onBackground = TextPrimary,
    surface = BackgroundSecondary,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundTertiary,
    onSurfaceVariant = TextSecondary,
    error = ErrorRed,
    onError = TextInverse,
    errorContainer = ErrorRed.copy(alpha = 0.15f),
    onErrorContainer = ErrorRed,
    outline = BorderSubtle,
    outlineVariant = DividerColor,
    scrim = Color.Black.copy(alpha = 0.6f)
)

@Composable
fun AiscernTheme(
    darkTheme: Boolean = true, // Always dark
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = AiscernTypography,
        shapes = AiscernShapes,
        content = content
    )
}
