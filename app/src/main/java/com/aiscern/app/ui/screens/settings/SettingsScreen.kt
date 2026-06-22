package com.aiscern.app.ui.screens.settings

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
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
import com.aiscern.app.theme.BackgroundPrimary
import com.aiscern.app.theme.BackgroundSecondary
import com.aiscern.app.theme.BackgroundTertiary
import com.aiscern.app.theme.BorderSubtle
import com.aiscern.app.theme.ErrorRed
import com.aiscern.app.theme.TextInverse
import com.aiscern.app.theme.TextMuted
import com.aiscern.app.theme.TextPrimary
import com.aiscern.app.theme.TextSecondary
import com.aiscern.app.theme.AiscernTypography
import com.aiscern.app.ui.components.AiscernCard
import com.aiscern.app.ui.components.AiscernTopAppBar

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundPrimary),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { AccountSection() }
        item { PreferencesSection() }
        item { PrivacySection() }
        item { AboutSection() }
    }
}

@Composable
private fun AccountSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.settings_account),
            style = AiscernTypography.headlineMedium,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        AiscernCard {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(AccentViolet),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Avatar",
                        tint = TextInverse,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "User Name",
                        style = AiscernTypography.titleLarge,
                        color = TextPrimary
                    )
                    Text(
                        text = "user@aiscern.com",
                        style = AiscernTypography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun PreferencesSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.settings_preferences),
            style = AiscernTypography.headlineMedium,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        AiscernCard {
            Column {
                ToggleSettingItem(
                    icon = Icons.Default.Brush,
                    title = stringResource(R.string.settings_dark_mode),
                    subtitle = stringResource(R.string.settings_dark_mode_hint),
                    checked = true,
                    onCheckedChange = { },
                    enabled = false
                )
                Divider()
                ToggleSettingItem(
                    icon = Icons.Default.Brush, // Replaced MotionPhotosOff
                    title = stringResource(R.string.settings_reduce_motion),
                    subtitle = null,
                    checked = false,
                    onCheckedChange = { }
                )
                Divider()
                NavigationSettingItem(
                    icon = Icons.Default.Language,
                    title = stringResource(R.string.settings_language),
                    value = "English"
                )
                Divider()
                NavigationSettingItem(
                    icon = Icons.Default.Search, // Replaced Scanner
                    title = stringResource(R.string.settings_default_modality),
                    value = "Text"
                )
            }
        }
    }
}

@Composable
private fun PrivacySection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.settings_privacy),
            style = AiscernTypography.headlineMedium,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        AiscernCard {
            Column {
                ToggleSettingItem(
                    icon = Icons.Default.Lock, // Replaced VisibilityOff
                    title = stringResource(R.string.settings_ephemeral),
                    subtitle = null,
                    checked = false,
                    onCheckedChange = { }
                )
                Divider()
                ToggleSettingItem(
                    icon = Icons.Default.Lock,
                    title = stringResource(R.string.settings_biometric),
                    subtitle = null,
                    checked = false,
                    onCheckedChange = { }
                )
                Divider()
                ActionSettingItem(
                    icon = Icons.Default.Delete,
                    title = stringResource(R.string.settings_clear_history),
                    isDestructive = true
                )
                Divider()
                ActionSettingItem(
                    icon = Icons.Default.Share, // Replaced Download
                    title = stringResource(R.string.settings_export_data),
                    isDestructive = false
                )
                Divider()
                ActionSettingItem(
                    icon = Icons.Default.Security,
                    title = stringResource(R.string.settings_delete_account),
                    isDestructive = true
                )
            }
        }
    }
}

@Composable
private fun AboutSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.settings_about),
            style = AiscernTypography.headlineMedium,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        AiscernCard {
            Column {
                Text(
                    text = stringResource(R.string.settings_version, "1.0.0"),
                    style = AiscernTypography.bodyMedium,
                    color = TextMuted,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                Divider()
                NavigationSettingItem(
                    icon = Icons.Default.Info, // Replaced Policy
                    title = stringResource(R.string.settings_terms),
                    value = null
                )
                Divider()
                NavigationSettingItem(
                    icon = Icons.Default.Info,
                    title = stringResource(R.string.settings_privacy_policy),
                    value = null
                )
                Divider()
                NavigationSettingItem(
                    icon = Icons.Default.Brush,
                    title = stringResource(R.string.settings_methodology),
                    value = null
                )
                Divider()
                NavigationSettingItem(
                    icon = Icons.Default.Email,
                    title = stringResource(R.string.settings_support),
                    value = null
                )
            }
        }
    }
}

@Composable
private fun ToggleSettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String?,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (enabled) TextSecondary else TextMuted,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = AiscernTypography.bodyMedium,
                color = if (enabled) TextPrimary else TextMuted
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = AiscernTypography.labelMedium,
                    color = TextMuted
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
    }
}

@Composable
private fun NavigationSettingItem(
    icon: ImageVector,
    title: String,
    value: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = AiscernTypography.bodyMedium,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
        if (value != null) {
            Text(
                text = value,
                style = AiscernTypography.labelMedium,
                color = TextMuted
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Navigate",
            tint = TextMuted,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun ActionSettingItem(
    icon: ImageVector,
    title: String,
    isDestructive: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isDestructive) ErrorRed else TextSecondary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = AiscernTypography.bodyMedium,
            color = if (isDestructive) ErrorRed else TextPrimary,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun Divider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(BorderSubtle)
    )
}
