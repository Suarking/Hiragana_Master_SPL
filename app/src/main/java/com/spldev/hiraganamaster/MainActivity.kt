package com.spldev.hiraganamaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.spldev.hiraganamaster.ui.navigation.AppNavigation
import com.spldev.hiraganamaster.ui.viewmodel.HiraganaViewModel
import com.spldev.hiraganamaster.ui.viewmodel.LoginViewModel
import com.spldev.hiraganamaster.ui.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

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

