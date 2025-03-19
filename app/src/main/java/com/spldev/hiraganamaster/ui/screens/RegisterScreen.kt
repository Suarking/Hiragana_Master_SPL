package com.spldev.hiraganamaster.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.spldev.hiraganamaster.R
import com.spldev.hiraganamaster.ui.viewmodel.LoginViewModel
import com.spldev.hiraganamaster.ui.viewmodel.RegisterViewModel
import com.spldev.hiraganamaster.ui.viewmodel.RegisterState

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

    BackHandler {
        loginViewModel.resetLoginState()
        onNavigateToLogin()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.title_register),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = {  Text(stringResource(R.string.text_hint_mail_register)) },
            placeholder = { Text(stringResource(R.string.text_placeholder_mail_register))  },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = {Text(stringResource(R.string.text_hint_password_register)) },
            placeholder = { Text(stringResource(R.string.text_placeholder_password_register))  },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.register(email, password) },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.text_button_sign_up))
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (registerState) {
            is RegisterState.Loading -> CircularProgressIndicator()
            is RegisterState.Success -> {
                LaunchedEffect(Unit) {
                    onRegisterSuccess() // Navegar cuando el registro sea exitoso
                }
                Text(text = (registerState as RegisterState.Success).message, color = Color.Green)
            }
            is RegisterState.Error -> Text(text = (registerState as RegisterState.Error).error, color = Color.Red)
            else -> {}
        }
    }
}
