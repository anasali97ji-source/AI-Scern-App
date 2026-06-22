package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.*
import com.example.ui.theme.*

sealed interface NavigationRoute {
    object Splash : NavigationRoute
    object Onboarding : NavigationRoute
    object SignIn : NavigationRoute
    object SignUp : NavigationRoute
    object ForgotPassword : NavigationRoute
    object MainTabs : NavigationRoute
    data class Detail(val result: ScanResult, val rawInput: String) : NavigationRoute
}

enum class TabItem {
    HOME, SCAN, HISTORY, ARIA
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Core persistence setup
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = HistoryRepository(database.historyDao())
        
        setContent {
            MyApplicationTheme {
                val viewModel: ScanViewModel by viewModels { ScanViewModelFactory(repository) }
                var currentRoute by remember { mutableStateOf<NavigationRoute>(NavigationRoute.Splash) }
                var activeTab by remember { mutableStateOf(TabItem.HOME) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = SpaceBackground
                ) {
                    com.example.ui.components.AuroraBackground {
                        AnimatedContent(
                            targetState = currentRoute,
                            transitionSpec = {
                                fadeIn() togetherWith fadeOut()
                            },
                            label = "Main_Navigation_Host"
                        ) { targetRoute ->
                            when (targetRoute) {
                                NavigationRoute.Splash -> {
                                    SplashScreen(
                                        onSplashComplete = {
                                            currentRoute = NavigationRoute.Onboarding
                                        }
                                    )
                                }
                                NavigationRoute.MainTabs -> {
                                    Scaffold(
                                        bottomBar = {
                                            NavigationBar(
                                                containerColor = CardSlate.copy(alpha = 0.95f),
                                                tonalElevation = 8.dp,
                                                windowInsets = WindowInsets.navigationBars
                                            ) {
                                            // Tab 1: HOME
                                            NavigationBarItem(
                                                selected = activeTab == TabItem.HOME,
                                                onClick = { activeTab = TabItem.HOME },
                                                icon = {
                                                    Icon(
                                                        imageVector = if (activeTab == TabItem.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                                                        contentDescription = "Home"
                                                    )
                                                },
                                                label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                                colors = NavigationBarItemDefaults.colors(
                                                    selectedIconColor = SpaceBackground,
                                                    selectedTextColor = ElectricCyan,
                                                    indicatorColor = ElectricCyan,
                                                    unselectedIconColor = TextGray,
                                                    unselectedTextColor = TextGray
                                                ),
                                                modifier = Modifier.testTag("nav_tab_home")
                                            )

                                            // Tab 2: SCAN
                                            NavigationBarItem(
                                                selected = activeTab == TabItem.SCAN,
                                                onClick = { activeTab = TabItem.SCAN },
                                                icon = {
                                                    Icon(
                                                        imageVector = if (activeTab == TabItem.SCAN) Icons.Filled.Radar else Icons.Outlined.Radar,
                                                        contentDescription = "SCAN ENGINE",
                                                        modifier = Modifier.size(32.dp),
                                                        tint = if (activeTab == TabItem.SCAN) SpaceBackground else ElectricCyan
                                                    )
                                                },
                                                label = { Text("Scan", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ElectricCyan) },
                                                colors = NavigationBarItemDefaults.colors(
                                                    selectedIconColor = SpaceBackground,
                                                    indicatorColor = ElectricCyan,
                                                    unselectedIconColor = ElectricCyan,
                                                    selectedTextColor = ElectricCyan
                                                ),
                                                modifier = Modifier.testTag("nav_tab_scan")
                                            )

                                            // Tab 3: HISTORY
                                            NavigationBarItem(
                                                selected = activeTab == TabItem.HISTORY,
                                                onClick = { activeTab = TabItem.HISTORY },
                                                icon = {
                                                    Icon(
                                                        imageVector = if (activeTab == TabItem.HISTORY) Icons.Filled.History else Icons.Outlined.History,
                                                        contentDescription = "History Tracker"
                                                    )
                                                },
                                                label = { Text("History", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                                colors = NavigationBarItemDefaults.colors(
                                                    selectedIconColor = SpaceBackground,
                                                    selectedTextColor = ElectricCyan,
                                                    indicatorColor = ElectricCyan,
                                                    unselectedIconColor = TextGray,
                                                    unselectedTextColor = TextGray
                                                ),
                                                modifier = Modifier.testTag("nav_tab_history")
                                            )

                                            // Tab 4: ARIA
                                            NavigationBarItem(
                                                selected = activeTab == TabItem.ARIA,
                                                onClick = { activeTab = TabItem.ARIA },
                                                icon = {
                                                    Icon(
                                                        imageVector = if (activeTab == TabItem.ARIA) Icons.Filled.ChatBubble else Icons.Outlined.ChatBubbleOutline,
                                                        contentDescription = "ARIA"
                                                    )
                                                },
                                                label = { Text("ARIA", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                                colors = NavigationBarItemDefaults.colors(
                                                    selectedIconColor = SpaceBackground,
                                                    selectedTextColor = ElectricCyan,
                                                    indicatorColor = ElectricCyan,
                                                    unselectedIconColor = TextGray,
                                                    unselectedTextColor = TextGray
                                                ),
                                                modifier = Modifier.testTag("nav_tab_aria")
                                            )
                                        }
                                    },
                                    containerColor = SpaceBackground // Important: transparent or background color
                                ) { innerPadding ->
                                    Box(
                                        modifier = Modifier
                                            .padding(innerPadding)
                                            .fillMaxSize()
                                    ) {
                                        when (activeTab) {
                                            TabItem.HOME -> {
                                                DashboardScreen(viewModel = viewModel)
                                            }
                                            TabItem.SCAN -> {
                                                ScanScreen(
                                                    viewModel = viewModel,
                                                    onNavigateToDetail = { result, rawInput ->
                                                        currentRoute = NavigationRoute.Detail(result, rawInput)
                                                    }
                                                )
                                            }
                                            TabItem.HISTORY -> {
                                                HistoryScreen(
                                                    viewModel = viewModel,
                                                    onNavigateToDetail = { result, rawInput ->
                                                        currentRoute = NavigationRoute.Detail(result, rawInput)
                                                    }
                                                )
                                            }
                                            TabItem.ARIA -> {
                                                // Temporarily link settings until Aria is fully standalone
                                                SettingsScreen(viewModel = viewModel)
                                            }
                                        }
                                    }
                                }
                            }
                            NavigationRoute.Onboarding -> {
                                OnboardingScreen(onFinish = { currentRoute = NavigationRoute.SignIn })
                            }
                            NavigationRoute.SignIn -> {
                                SignInScreen(
                                    onSignInSuccess = { currentRoute = NavigationRoute.MainTabs },
                                    onNavigateSignUp = { currentRoute = NavigationRoute.SignUp },
                                    onNavigateForgot = { currentRoute = NavigationRoute.ForgotPassword }
                                )
                            }
                            NavigationRoute.SignUp -> {
                                // Fallback directly to SignIn
                                SignInScreen(
                                    onSignInSuccess = { currentRoute = NavigationRoute.MainTabs },
                                    onNavigateSignUp = { currentRoute = NavigationRoute.SignUp },
                                    onNavigateForgot = { currentRoute = NavigationRoute.ForgotPassword }
                                )
                            }
                            NavigationRoute.ForgotPassword -> {
                                // Fallback directly to SignIn
                                SignInScreen(
                                    onSignInSuccess = { currentRoute = NavigationRoute.MainTabs },
                                    onNavigateSignUp = { currentRoute = NavigationRoute.SignUp },
                                    onNavigateForgot = { currentRoute = NavigationRoute.ForgotPassword }
                                )
                            }
                            is NavigationRoute.Detail -> {
                                ResultScreen(
                                    result = targetRoute.result,
                                    rawInput = targetRoute.rawInput,
                                    onNavigateHome = {
                                        currentRoute = NavigationRoute.MainTabs
                                        activeTab = TabItem.HOME
                                    }
                                )
                            }
                        }
                    }
                    }
                }
            }
        }
    }
}
