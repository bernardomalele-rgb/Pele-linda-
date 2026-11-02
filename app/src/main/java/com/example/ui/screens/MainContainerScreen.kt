package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.navigation.Screen
import com.example.ui.screens.diary.DiaryScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.recipes.RecipesScreen
import com.example.ui.screens.scan.FacialScanScreen
import com.example.ui.theme.*
import com.example.ui.viewmodel.PeleLindaViewModel

@Composable
fun MainContainerScreen(viewModel: PeleLindaViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Rotina.route

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 6.dp,
                windowInsets = WindowInsets.navigationBars,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("main_bottom_nav_bar")
            ) {
                Screen.bottomNavItems.forEach { screen ->
                    val selected = currentRoute == screen.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (selected) screen.selectedIcon else screen.unselectedIcon,
                                contentDescription = screen.title,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = screen.title,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = RosePrimary,
                            selectedTextColor = RosePrimary,
                            indicatorColor = RoseContainer,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("nav_item_${screen.route}")
                    )
                }
            }
        },
        containerColor = CreamBackground
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Rotina.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            composable(Screen.Rotina.route) {
                HomeScreen(viewModel = viewModel)
            }
            composable(Screen.Receitas.route) {
                RecipesScreen(viewModel = viewModel)
            }
            composable(Screen.Scan.route) {
                FacialScanScreen(viewModel = viewModel)
            }
            composable(Screen.Diario.route) {
                DiaryScreen(viewModel = viewModel)
            }
            composable(Screen.Perfil.route) {
                ProfileScreen(viewModel = viewModel)
            }
        }
    }
}
