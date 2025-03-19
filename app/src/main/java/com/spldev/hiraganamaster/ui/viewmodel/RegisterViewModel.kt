package com.spldev.hiraganamaster.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spldev.hiraganamaster.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // Anotar el ViewModel para que Hilt pueda inyectar dependencias en él
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository // Inyectar el repositorio de autenticación
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> get() = _registerState

    fun register(email: String, password: String) {
        _registerState.value = RegisterState.Loading

        viewModelScope.launch {
            authRepository.register(email, password) { success, errorMessage ->
                if (success) {
                    _registerState.value = RegisterState.Success("Registro exitoso")
                } else {
                    _registerState.value = RegisterState.Error(errorMessage ?: "Error desconocido")
                }
            }
        }
    }
}

// Clase sellada para representar los diferentes estados del registro
sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val message: String) : RegisterState()
    data class Error(val error: String) : RegisterState()
}
