package com.aiscern.app.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aiscern.app.ui.screens.dashboard.DashboardScreen
import com.aiscern.app.ui.screens.detect.DetectScreen
import com.aiscern.app.ui.screens.history.HistoryScreen
import com.aiscern.app.ui.screens.home.HomeScreen
import com.aiscern.app.ui.screens.onboarding.OnboardingScreen
import com.aiscern.app.ui.screens.result.ResultScreen
import com.aiscern.app.ui.screens.settings.SettingsScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aiscern.app.di.AppContainer
import com.aiscern.app.ui.screens.detect.DetectViewModel

@Composable
fun AiscernNavHost(
    navController: NavHostController,
    startDestination: String,
    appContainer: AppContainer,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = { slideInHorizontally { it } + fadeIn() },
        exitTransition = { slideOutHorizontally { -it } + fadeOut() },
        popEnterTransition = { slideInHorizontally { -it } + fadeIn() },
        popExitTransition = { slideOutHorizontally { it } + fadeOut() }
    ) {
        composable("onboarding") {
            OnboardingScreen(
                onComplete = {
                    navController.navigate("home") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            HomeScreen(
                onStartScan = { navController.navigate("detect") },
                onViewHistory = { navController.navigate("history") }
            )
        }
        composable("detect") {
            val viewModel: DetectViewModel = viewModel(
                factory = DetectViewModel.provideFactory(appContainer.detectionRepository)
            )
            DetectScreen(
                viewModel = viewModel,
                onResultReady = { scanId ->
                    navController.navigate("result/$scanId")
                }
            )
        }
        composable("result/{scanId}") { backStackEntry ->
            val scanId = backStackEntry.arguments?.getString("scanId") ?: ""
            ResultScreen(
                scanId = scanId,
                onBack = { navController.popBackStack() },
                onNewScan = { navController.navigate("detect") }
            )
        }
        composable("history") {
            HistoryScreen(
                onScanClick = { scanId ->
                    navController.navigate("result/$scanId")
                }
            )
        }
        composable("dashboard") {
            DashboardScreen()
        }
        composable("settings") {
            SettingsScreen()
        }
    }
}
