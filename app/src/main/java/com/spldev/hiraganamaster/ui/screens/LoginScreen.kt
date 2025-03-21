package com.spldev.hiraganamaster.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.spldev.hiraganamaster.MainActivity
import com.spldev.hiraganamaster.R
import com.spldev.hiraganamaster.common.composables.LogoHeader
import com.spldev.hiraganamaster.ui.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState()
    val context = LocalContext.current

    // Paleta de colores inspirada en Japón (rojo, blanco, negro y tonos naturales)
    val backgroundColor = Color(0xFFF8E1D4) // Color beige inspirado en papel japonés tradicional
    val accentColor = Color(0xFFB22222) // Rojo japonés (similar al rojo de los torii gates)
    val textColor = Color(0xFF2C2C2C) // Negro suave para texto

    BackHandler {
        // Cerrar la aplicación al presionar "Atrás"
        (context as? Activity)?.finishAndRemoveTask()
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
                        text = stringResource(R.string.title_login),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = accentColor // Rojo japonés como color principal del título
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(stringResource(R.string.text_hint_mail)) },
                        placeholder = { Text(stringResource(R.string.text_placeholder_mail)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(stringResource(R.string.text_hint_password)) },
                        placeholder = { Text(stringResource(R.string.text_placeholder_password)) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier=Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick={viewModel.login(email,password)},
                        colors=ButtonDefaults.buttonColors(containerColor=accentColor,contentColor=Color.White),
                        shape=RoundedCornerShape(8.dp),
                        modifier=Modifier.fillMaxWidth()
                    ) {
                        Text(text=stringResource(R.string.text_button_sign_in))
                    }

                    TextButton(
                        onClick=onNavigateToRegister,
                        colors=ButtonDefaults.textButtonColors(contentColor=accentColor)
                    ) {
                        Text(text=stringResource(R.string.text_button_register))
                    }

                    when(loginState){
                        is LoginViewModel.LoginState.Loading->CircularProgressIndicator(color=accentColor)
                        is LoginViewModel.LoginState.Success->{
                            LaunchedEffect(Unit){
                                onLoginSuccess()
                            }
                            Text(text=(loginState as LoginViewModel.LoginState.Success).message,color=textColor)
                        }
                        is LoginViewModel.LoginState.Error->Text(text=(loginState as LoginViewModel.LoginState.Error).error,color=textColor)
                        else->{}
                    }
                }
            }
        }
    }
}
