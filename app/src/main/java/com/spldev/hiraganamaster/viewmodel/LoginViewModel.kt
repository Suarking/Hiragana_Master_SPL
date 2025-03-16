package com.spldev.hiraganamaster.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Estados para manejar el resultado del login
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> get() = _loginState

    fun login(email: String, password: String) {
        // Validar campos vacíos
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("El correo y la contraseña no pueden estar vacíos")
            return
        }

        _loginState.value = LoginState.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginState.value = LoginState.Success("Login exitoso")
                } else {
                    _loginState.value = LoginState.Error(task.exception?.message ?: "Error desconocido")
                }
            }
    }

    fun resetLoginState() {
        _loginState.value = LoginState.Idle // Restablecer el estado a Idle
    }


    // Clase sellada para representar los diferentes estados del login
    sealed class LoginState {
        object Idle : LoginState() // Estado inicial
        object Loading : LoginState() // Estado de carga
        data class Success(val message: String) : LoginState() // Estado de éxito
        data class Error(val error: String) : LoginState() // Estado de error
    }
}
