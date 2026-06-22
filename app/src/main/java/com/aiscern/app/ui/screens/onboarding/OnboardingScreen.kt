package com.aiscern.app.ui.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.aiscern.app.theme.TextInverse
import com.aiscern.app.theme.TextMuted
import com.aiscern.app.theme.TextPrimary
import com.aiscern.app.theme.TextSecondary
import com.aiscern.app.theme.AiscernTypography
import com.aiscern.app.ui.components.AiscernPrimaryButton
import com.aiscern.app.ui.components.AiscernSecondaryButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundPrimary)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> OnboardingWelcomePage(
                    onGetStarted = {
                        scope.launch { pagerState.animateScrollToPage(1) }
                    },
                    onSkip = onComplete
                )
                1 -> OnboardingFeaturesPage(
                    onNext = {
                        scope.launch { pagerState.animateScrollToPage(2) }
                    },
                    onPrevious = {
                        scope.launch { pagerState.animateScrollToPage(0) }
                    }
                )
                2 -> OnboardingPermissionsPage(
                    onComplete = onComplete
                )
            }
        }

        // Page indicator
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(3) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .size(if (isSelected) 24.dp else 8.dp, 8.dp)
                        .background(
                            color = if (isSelected) AccentViolet else BackgroundTertiary,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Composable
private fun OnboardingWelcomePage(
    onGetStarted: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Gradient mesh background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                AccentViolet.copy(alpha = 0.1f),
                                BackgroundPrimary
                            ),
                            center = Offset(size.width * 0.3f, size.height * 0.3f),
                            radius = size.width * 0.6f
                        )
                    )
                }
        )

        TextButton(
            onClick = onSkip,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.onboarding_skip),
                style = AiscernTypography.labelLarge,
                color = TextMuted
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animated gradient logo text
            Text(
                text = "Aiscern",
                style = AiscernTypography.displayLarge,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.drawBehind {
                    drawRect(
                        brush = AccentGradient,
                        size = size
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.onboarding_welcome_subtitle),
                style = AiscernTypography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            AiscernPrimaryButton(
                text = stringResource(R.string.onboarding_get_started),
                onClick = onGetStarted,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun OnboardingFeaturesPage(
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    modifier: Modifier = Modifier
) {
    val features = listOf(
        Triple(Icons.AutoMirrored.Filled.List, "Text Detection", "Identify AI-generated text from ChatGPT, Claude, and more"),
        Triple(Icons.Default.Image, "Image Detection", "Spot deepfakes and AI-generated images from Midjourney, DALL-E"),
        Triple(Icons.Default.MusicNote, "Audio Detection", "Detect synthetic voices and AI-generated audio clips"),
        Triple(Icons.Default.Videocam, "Video Detection", "Analyze videos frame-by-frame for deepfake content")
    )

    val pagerState = rememberPagerState(pageCount = { features.size })

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            val (icon, title, desc) = features[page]
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(80.dp),
                    tint = AccentViolet
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = title,
                    style = AiscernTypography.headlineMedium,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = desc,
                    style = AiscernTypography.bodyLarge,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Feature dots
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 24.dp)
        ) {
            repeat(features.size) { index ->
                Box(
                    modifier = Modifier
                        .size(if (pagerState.currentPage == index) 24.dp else 8.dp, 8.dp)
                        .background(
                            color = if (pagerState.currentPage == index) AccentViolet else BackgroundTertiary,
                            shape = CircleShape
                        )
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AiscernSecondaryButton(
                text = stringResource(R.string.onboarding_previous),
                onClick = onPrevious,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            AiscernPrimaryButton(
                text = stringResource(R.string.onboarding_next),
                onClick = onNext,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun OnboardingPermissionsPage(
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = stringResource(R.string.onboarding_permissions_title),
            style = AiscernTypography.headlineLarge,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Permission rows would go here with toggle switches
        // Simplified for this chunk

        Spacer(modifier = Modifier.weight(1f))

        AiscernPrimaryButton(
            text = stringResource(R.string.onboarding_enable_continue),
            onClick = onComplete,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
