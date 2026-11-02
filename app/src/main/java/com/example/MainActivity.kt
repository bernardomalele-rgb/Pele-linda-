package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.ui.screens.MainContainerScreen
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.admin.AdminLoginScreen
import com.example.ui.screens.auth.InitialQuizScreen
import com.example.ui.screens.auth.RegisterScreen
import com.example.ui.screens.auth.SmsVerificationScreen
import com.example.ui.screens.auth.WelcomeScreen
import com.example.ui.screens.subscription.SubscriptionScreen
import com.example.ui.theme.PeleLindaTheme
import com.example.ui.viewmodel.PeleLindaViewModel
import com.example.ui.viewmodel.ScreenDestination

class MainActivity : ComponentActivity() {

    private val viewModel: PeleLindaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PeleLindaTheme {
                val currentScreen by viewModel.currentScreen.collectAsState()
                val snackbarMessage by viewModel.snackbarMessage.collectAsState()
                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(snackbarMessage) {
                    snackbarMessage?.let { msg ->
                        snackbarHostState.showSnackbar(msg)
                        viewModel.clearSnackbar()
                    }
                }

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
                            when (screen) {
                                is ScreenDestination.Welcome -> WelcomeScreen(viewModel)
                                is ScreenDestination.Register -> {
                                    BackHandler { viewModel.navigateTo(ScreenDestination.Welcome) }
                                    RegisterScreen(viewModel)
                                }
                                is ScreenDestination.SmsVerification -> {
                                    BackHandler { viewModel.navigateTo(ScreenDestination.Register) }
                                    SmsVerificationScreen(viewModel)
                                }
                                is ScreenDestination.InitialQuiz -> {
                                    InitialQuizScreen(viewModel)
                                }
                                is ScreenDestination.MainTabs -> {
                                    MainContainerScreen(viewModel)
                                }
                                is ScreenDestination.SubscriptionPaywall -> {
                                    SubscriptionScreen(viewModel, isBlockingPaywall = true)
                                }
                                is ScreenDestination.AdminLogin -> {
                                    BackHandler { viewModel.navigateTo(ScreenDestination.MainTabs) }
                                    AdminLoginScreen(viewModel)
                                }
                                is ScreenDestination.AdminDashboard -> {
                                    BackHandler { viewModel.logoutAdmin() }
                                    AdminDashboardScreen(viewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
