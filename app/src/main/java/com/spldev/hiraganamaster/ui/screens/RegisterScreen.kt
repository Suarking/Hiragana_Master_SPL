package com.spldev.hiraganamaster.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.spldev.hiraganamaster.R
import com.spldev.hiraganamaster.common.composables.LogoHeader
import com.spldev.hiraganamaster.ui.viewmodel.LoginViewModel
import com.spldev.hiraganamaster.ui.viewmodel.RegisterState
import com.spldev.hiraganamaster.ui.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onRegisterSuccess: () -> Unit, // Callback para manejar el éxito del registro
    loginViewModel: LoginViewModel,
    onNavigateToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val registerState by viewModel.registerState.collectAsState()

    // Paleta de colores inspirada en Japón (rojo, blanco, negro y tonos naturales)
    val backgroundColor = Color(0xFFF8E1D4) // Color beige inspirado en papel japonés tradicional
    val accentColor = Color(0xFFB22222) // Rojo japonés (similar al rojo de los torii gates)
    val textColor = Color(0xFF2C2C2C) // Negro suave para texto

    BackHandler {
        loginViewModel.resetLoginState()
        onNavigateToLogin()
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor // Fondo inspirado en papel japonés tradicional
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(72.dp)) // Espaciado adicional para separar del borde superior

            LogoHeader() // Logotipo en la parte superior

            Spacer(modifier = Modifier.height(16.dp)) // Espaciado adicional para separar del borde superior

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.title_register),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = accentColor // Rojo japonés como color principal del título
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(stringResource(R.string.text_hint_mail_register)) },
                        placeholder = { Text(stringResource(R.string.text_placeholder_mail_register)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(stringResource(R.string.text_hint_password_register)) },
                        placeholder = { Text(stringResource(R.string.text_placeholder_password_register)) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = { viewModel.register(email, password) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accentColor,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.text_button_sign_up))
                    }

                    TextButton(
                        onClick = onNavigateToLogin,
                        colors = ButtonDefaults.textButtonColors(contentColor = accentColor)
                    ) {
                        Text(text = stringResource(R.string.text_button_back_login))
                    }
                    when (registerState) {
                        is RegisterState.Loading -> CircularProgressIndicator(color = accentColor)
                        is RegisterState.Success -> {
                            LaunchedEffect(Unit) {
                                onRegisterSuccess() // Navegar cuando el registro sea exitoso
                            }
                            Text(
                                text = (registerState as RegisterState.Success).message,
                                color = textColor
                            )
                        }

                        is RegisterState.Error -> Text(
                            text = (registerState as RegisterState.Error).error,
                            color = textColor
                        )

                        else -> {}
                    }
                }
            }
        }
    }
}
