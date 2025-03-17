package com.spldev.hiraganamaster.viewmodel


import androidx.lifecycle.ViewModel
import com.spldev.hiraganamaster.datasource.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel // Anotar el ViewModel para que Hilt pueda inyectar dependencias en él
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository // Inyectar el repositorio de autenticación
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> get() = _loginState

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("El correo y la contraseña no pueden estar vacíos")
            return
        }

        _loginState.value = LoginState.Loading

        authRepository.login(email, password) { success, errorMessage ->
            if (success) {
                _loginState.value = LoginState.Success("Login exitoso")
            } else {
                _loginState.value = LoginState.Error(errorMessage ?: "Error desconocido")
            }
        }
    }

    fun resetLoginState() {
        _loginState.value = LoginState.Idle // Restablecer el estado a Idle
    }

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        data class Success(val message: String) : LoginState()
        data class Error(val error: String) : LoginState()
    }
}
