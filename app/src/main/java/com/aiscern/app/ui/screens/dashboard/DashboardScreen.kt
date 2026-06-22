package com.aiscern.app.ui.screens.dashboard

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiscern.app.R
import com.aiscern.app.theme.AccentGradient
import com.aiscern.app.theme.AccentViolet
import com.aiscern.app.theme.BackgroundPrimary
import com.aiscern.app.theme.BackgroundSecondary
import com.aiscern.app.theme.BackgroundTertiary
import com.aiscern.app.theme.BorderSubtle
import com.aiscern.app.theme.ErrorRed
import com.aiscern.app.theme.SuccessGreen
import com.aiscern.app.theme.TextInverse
import com.aiscern.app.theme.TextMuted
import com.aiscern.app.theme.TextPrimary
import com.aiscern.app.theme.TextSecondary
import com.aiscern.app.theme.AiscernTypography
import com.aiscern.app.ui.components.AiscernCard
import com.aiscern.app.ui.components.AiscernPrimaryButton
import com.aiscern.app.ui.components.AiscernTopAppBar

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundPrimary),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { CreditCard(remaining = 12, total = 15, resetTime = "8 hours") }
        item { UsageChart() }
        item { PlanStatusCard() }
        item { RecentActivitySection() }
    }
}

@Composable
private fun CreditCard(
    remaining: Int,
    total: Int,
    resetTime: String,
    modifier: Modifier = Modifier
) {
    val progress = remaining.toFloat() / total.toFloat()

    AiscernCard(
        modifier = modifier.fillMaxWidth(),
        showGradientBorder = true
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.dashboard_credits_label),
                style = AiscernTypography.labelLarge,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = remaining.toString(),
                style = AiscernTypography.displayLarge,
                color = TextPrimary
            )

            Text(
                text = stringResource(R.string.dashboard_credits_of, total),
                style = AiscernTypography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(BackgroundSecondary)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(AccentGradient)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.dashboard_credits_reset, resetTime),
                style = AiscernTypography.labelMedium,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.dashboard_upgrade),
                style = AiscernTypography.labelLarge,
                color = AccentViolet
            )
        }
    }
}

@Composable
private fun UsageChart(modifier: Modifier = Modifier) {
    val days = listOf("Mon" to 3, "Tue" to 5, "Wed" to 2, "Thu" to 8, "Fri" to 6, "Sat" to 4, "Sun" to 1)
    val maxValue = days.maxOf { it.second }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.dashboard_usage),
            style = AiscernTypography.headlineMedium,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            days.forEach { (day, value) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = value.toString(),
                        style = AiscernTypography.labelMedium,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height((if (value == 0) 2 else (value.toFloat() / maxValue * 120).toInt()).dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(AccentViolet.copy(alpha = 0.8f))
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = day,
                        style = AiscernTypography.labelMedium,
                        color = TextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.dashboard_total_scans, 47),
            style = AiscernTypography.bodyMedium,
            color = TextSecondary
        )
    }
}

@Composable
private fun PlanStatusCard(modifier: Modifier = Modifier) {
    AiscernCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            Text(
                text = stringResource(R.string.dashboard_plan_free),
                style = AiscernTypography.headlineMedium,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            val features = listOf(
                Pair("15 scans/day", true),
                Pair("Text detection", true),
                Pair("Image detection", true),
                Pair("Audio detection", true),
                Pair("Video detection", true),
                Pair("Batch detection", false),
                Pair("API access", false),
                Pair("Priority support", false)
            )

            features.forEach { (feature, included) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = if (included) Icons.Default.Check else Icons.Default.Close,
                        contentDescription = null,
                        tint = if (included) SuccessGreen else TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = feature,
                        style = AiscernTypography.bodyMedium,
                        color = if (included) TextPrimary else TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AiscernPrimaryButton(
                text = stringResource(R.string.dashboard_upgrade),
                onClick = { /* Navigate to upgrade */ },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun RecentActivitySection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Recent Activity",
            style = AiscernTypography.headlineMedium,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Mini list of last 5 scans
        repeat(3) { index ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(BackgroundSecondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "T",
                        style = AiscernTypography.labelMedium,
                        color = AccentViolet
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Text scan #${100 + index}",
                        style = AiscernTypography.bodyMedium,
                        color = TextPrimary
                    )
                    Text(
                        text = "${index + 1} hour ago",
                        style = AiscernTypography.labelMedium,
                        color = TextMuted
                    )
                }
                Text(
                    text = "${(70 + index * 5)}%",
                    style = AiscernTypography.labelMedium,
                    color = ErrorRed
                )
            }
        }
    }
}
