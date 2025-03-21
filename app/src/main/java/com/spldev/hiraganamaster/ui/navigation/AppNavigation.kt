package com.spldev.hiraganamaster.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.mlkit.vision.digitalink.Ink
import com.spldev.hiraganamaster.ui.screens.GuessTheKana
import com.spldev.hiraganamaster.ui.screens.HiraganaPracticeScreen
import com.spldev.hiraganamaster.ui.screens.LoginScreen
import com.spldev.hiraganamaster.ui.screens.RegisterScreen
import com.spldev.hiraganamaster.ui.screens.SelectionScreen
import com.spldev.hiraganamaster.ui.viewmodel.HiraganaViewModel
import com.spldev.hiraganamaster.ui.viewmodel.LoginViewModel
import com.spldev.hiraganamaster.ui.viewmodel.RegisterViewModel

@Composable
fun AppNavigation(
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel,
    hiraganaViewModel: HiraganaViewModel,
    context: Context // Contexto necesario para configurar el modelo en ViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    hiraganaViewModel.setupDigitalInkRecognition(context)
                    navController.navigate("selection")
                },
                onNavigateToRegister = {
                    navController.navigate("register") // Navegar a la pantalla de registro.
                }
            )
        }

        composable("register") {
            RegisterScreen(
                viewModel = registerViewModel,
                loginViewModel = loginViewModel,
                onRegisterSuccess = {
                    navController.navigate("login") // Navegar a la pantalla de login después del registro exitoso.
                },
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            )
        }

        composable("selection") {
            SelectionScreen(
                hiraganaViewModel = hiraganaViewModel,
                onPlayKanaPractice = {
                    navController.navigate("hiraganaPractice")
                },
                onPlayGuessTheKana = {
                    navController.navigate("guessTheKana")
                },
                onLogout = {
                    loginViewModel.logout()
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                    loginViewModel.resetLoginState() // Restablecer el estado del login
                }
            )
        }

        composable("hiraganaPractice") {
            HiraganaPracticeScreen(
                viewModel = hiraganaViewModel,
                onBack = {
                    navController.navigate("selection") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                onVerifyDrawing = {
                    val inkBuilder = Ink.builder()
                    hiraganaViewModel.strokes.forEach { stroke ->
                        val strokeBuilder =
                            Ink.Stroke.builder()
                        stroke.forEach { point ->
                            strokeBuilder.addPoint(Ink.Point.create(point.x, point.y))
                        }
                        inkBuilder.addStroke(strokeBuilder.build())
                    }
                    val ink =
                        inkBuilder.build()

                    hiraganaViewModel.verifyDrawing(context, ink)
                },
                onNextCharacter =
                    { hiraganaViewModel.changeCharacter() },
                onClearCanvas =
                    { hiraganaViewModel.clearCanvas() })
        }

        composable("guessTheKana") {
            GuessTheKana()
        }
    }
}
