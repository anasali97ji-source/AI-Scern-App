package com.aiscern.app.ui.screens.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.aiscern.app.theme.SuccessGreen
import com.aiscern.app.theme.ErrorRed
import com.aiscern.app.theme.TextInverse
import com.aiscern.app.theme.TextMuted
import com.aiscern.app.theme.TextPrimary
import com.aiscern.app.theme.TextSecondary
import com.aiscern.app.theme.AiscernTypography
import com.aiscern.app.ui.components.AiscernCard
import com.aiscern.app.ui.components.AiscernPrimaryButton
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    onStartScan: () -> Unit,
    onViewHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundPrimary),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item { HeroSection(onStartScan = onStartScan) }
        item { StatsRow() }
        item { GallerySection() }
        item { IndustriesSection() }
        item { CTASection(onStartScan = onStartScan) }
    }
}

@Composable
private fun HeroSection(
    onStartScan: () -> Unit,
    modifier: Modifier = Modifier
) {
    val headlines = listOf(
        "Detect AI Text",
        "Spot Deepfakes",
        "Verify Authenticity",
        "Analyze Audio"
    )
    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            currentIndex = (currentIndex + 1) % headlines.size
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)
            .drawBehind {
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            AccentViolet.copy(alpha = 0.08f),
                            BackgroundPrimary
                        ),
                        center = Offset(size.width * 0.5f, size.height * 0.3f),
                        radius = size.width * 0.7f
                    )
                )
            }
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(
                targetState = currentIndex,
                transitionSpec = {
                    (slideInVertically { it } + fadeIn(tween(500)))
                        .togetherWith(slideOutVertically { -it } + fadeOut(tween(500)))
                },
                label = "headline_animation"
            ) { index ->
                Text(
                    text = headlines[index],
                    style = AiscernTypography.displayLarge,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.home_subtitle),
                style = AiscernTypography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun StatsRow(modifier: Modifier = Modifier) {
    val stats = listOf(
        Triple(Icons.Default.Shield, stringResource(R.string.home_stats_models), "8+"),
        Triple(Icons.Default.Star, stringResource(R.string.home_stats_accuracy), "95%"),
        Triple(Icons.Default.Biotech, stringResource(R.string.home_stats_modalities), "4")
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        stats.forEach { (icon, label, value) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = AccentViolet,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    style = AiscernTypography.titleLarge,
                    color = TextPrimary
                )
                Text(
                    text = label,
                    style = AiscernTypography.labelMedium,
                    color = TextMuted
                )
            }
        }
    }
}

@Composable
private fun GallerySection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.home_gallery_title),
            style = AiscernTypography.headlineMedium,
            color = TextPrimary,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(6) { index ->
                val isAi = index < 3
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BackgroundSecondary)
                ) {
                    // Placeholder for image
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                if (isAi) AccentViolet.copy(alpha = 0.2f)
                                else SuccessGreen.copy(alpha = 0.2f)
                            )
                    )
                    // Badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(
                                color = if (isAi) ErrorRed else SuccessGreen,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (isAi) "AI" else "✓",
                            style = AiscernTypography.labelMedium,
                            color = TextInverse
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun IndustriesSection(modifier: Modifier = Modifier) {
    val industries = listOf(
        Triple(Icons.Default.Newspaper, "Media & Press", "Verify images and audio before publishing"),
        Triple(Icons.Default.School, "Education", "Detect AI-written student submissions"),
        Triple(Icons.Default.Gavel, "Legal", "Authenticate evidence and documents"),
        Triple(Icons.Default.Business, "HR & Recruiting", "Screen AI-polished CVs and cover letters"),
        Triple(Icons.Default.Security, "Security", "Detect voice-cloned fraud calls"),
        Triple(Icons.Default.Psychology, "Research", "Validate source material integrity"),
        Triple(Icons.Default.HealthAndSafety, "Healthcare", "Verify medical image authenticity"),
        Triple(Icons.Default.Balance, "Marketing", "Audit user-generated content")
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.home_industries_title),
            style = AiscernTypography.headlineMedium,
            color = TextPrimary,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        industries.forEach { (icon, title, desc) ->
            AiscernCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = AccentViolet,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            style = AiscernTypography.titleLarge,
                            color = TextPrimary
                        )
                        Text(
                            text = desc,
                            style = AiscernTypography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CTASection(
    onStartScan: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AiscernPrimaryButton(
            text = stringResource(R.string.home_cta_scan),
            onClick = onStartScan,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
