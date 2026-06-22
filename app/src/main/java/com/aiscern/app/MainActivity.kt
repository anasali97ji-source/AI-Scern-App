package com.aiscern.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aiscern.app.navigation.AiscernNavHost
import com.aiscern.app.theme.AiscernTheme
import com.aiscern.app.ui.components.AiscernBottomNavigation
import com.aiscern.app.di.AppContainer

class MainActivity : ComponentActivity() {
    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        appContainer = AppContainer(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AiscernTheme {
                AiscernApp(appContainer)
            }
        }
    }
}

@Composable
fun AiscernApp(appContainer: AppContainer) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    val showBottomNav = currentRoute in listOf("home", "detect", "history", "dashboard", "settings")
    var startDestination by rememberSaveable { mutableStateOf("home") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomNav) {
                AiscernBottomNavigation(
                    currentRoute = currentRoute,
                    onItemSelected = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        AiscernNavHost(
            navController = navController,
            startDestination = startDestination,
            appContainer = appContainer,
            modifier = Modifier.padding(paddingValues)
        )
    }
}
