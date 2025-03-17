package com.spldev.hiraganamaster

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.mlkit.vision.digitalink.Ink
import com.spldev.hiraganamaster.ui.screens.HiraganaPracticeScreen
import com.spldev.hiraganamaster.ui.screens.LoginScreen
import com.spldev.hiraganamaster.ui.screens.RegisterScreen
import com.spldev.hiraganamaster.viewmodel.HiraganaViewModel
import com.spldev.hiraganamaster.viewmodel.LoginViewModel
import com.spldev.hiraganamaster.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.viewmodel.compose.viewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {

            val loginViewModel: LoginViewModel by viewModels()
            val registerViewModel: RegisterViewModel by viewModels()
            val hiraganaViewModel: HiraganaViewModel = viewModel()

            AppNavigation(
                loginViewModel,
                registerViewModel,
                hiraganaViewModel,
                this // Pasar contexto para configurar el modelo al navegar
            )
        }
    }
}


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
                    navController.navigate("hiraganaPractice")
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
                    navController.navigate("login") // Navegar a la pantalla de login después del registro exitoso.
                }
            )
        }

        composable("hiraganaPractice") {
            HiraganaPracticeScreen(
                viewModel = hiraganaViewModel,
                onLogout = {
                    FirebaseAuth.getInstance().signOut() // Cerrar sesión en Firebase
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                    loginViewModel.resetLoginState() // Restablecer el estado del login
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
    }
}
