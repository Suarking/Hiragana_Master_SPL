package com.spldev.hiraganamaster

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.mlkit.vision.digitalink.Ink
import com.spldev.hiraganamaster.ui.screens.LoginScreen
import com.spldev.hiraganamaster.viewmodel.HiraganaViewModel
import com.spldev.hiraganamaster.viewmodel.LoginViewModel



class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loginViewModel = LoginViewModel()
        val hiraganaViewModel = HiraganaViewModel()

        setContent {
            AppNavigation(
                loginViewModel = loginViewModel,
                hiraganaViewModel = hiraganaViewModel,
                context = this // Pasar contexto para configurar el modelo al navegar
            )
        }
    }
}

@Composable
fun AppNavigation(
    loginViewModel: LoginViewModel,
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
                    // Configurar el modelo antes de navegar a HiraganaPracticeScreen
                    hiraganaViewModel.setupDigitalInkRecognition(context)
                    navController.navigate("hiraganaPractice")
                }
            )
        }

        composable("hiraganaPractice") {
            HiraganaPracticeScreen(
                viewModel = hiraganaViewModel,
                context = context,
                onVerifyDrawing = {
                    val inkBuilder = Ink.builder()
                    hiraganaViewModel.strokes.forEach { stroke ->
                        val strokeBuilder = Ink.Stroke.builder()
                        stroke.forEach { point ->
                            strokeBuilder.addPoint(Ink.Point.create(point.x, point.y))
                        }
                        inkBuilder.addStroke(strokeBuilder.build())
                    }
                    val ink = inkBuilder.build()

                    hiraganaViewModel.verifyDrawing(context, ink)
                },
                onNextCharacter = { hiraganaViewModel.changeCharacter() },
                onClearCanvas = { hiraganaViewModel.clearCanvas() }
            )
        }
    }
}