package com.aiscern.app.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Backgrounds
val BackgroundPrimary = Color(0xFF0A0A0F)
val BackgroundSecondary = Color(0xFF12121A)
val BackgroundTertiary = Color(0xFF1A1A24)
val BackgroundElevated = Color(0xFF242430)

// Accents
val AccentViolet = Color(0xFF8B5CF6)
val AccentBlue = Color(0xFF3B82F6)
val AccentGradient = Brush.horizontalGradient(listOf(AccentViolet, AccentBlue))
val AccentGradientVertical = Brush.verticalGradient(listOf(AccentViolet, AccentBlue))

// Semantic
val SuccessGreen = Color(0xFF10B981)
val WarningAmber = Color(0xFFF59E0B)
val ErrorRed = Color(0xFFEF4444)
val InfoCyan = Color(0xFF06B6D4)

// Text
val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted = Color(0xFF64748B)
val TextInverse = Color(0xFF0A0A0F)

// Borders & Dividers
val BorderSubtle = Color(0xFFFFFFFF).copy(alpha = 0.08f)
val BorderGlow = AccentViolet.copy(alpha = 0.15f)
val DividerColor = Color(0xFF2A2A3A)
