package es.javier.cappcake.presentation.loginscreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LoginScreenViewModel() : ViewModel() {

    val emailField: MutableState<String> = mutableStateOf("")
    val passwordField: MutableState<String> = mutableStateOf("")
    val loginButtonEnabled: State<Boolean> get() = mutableStateOf(passwordField.value.isNotBlank() && emailField.value.isNotBlank())

    suspend fun validateUser() : Boolean {
        delay(2000)
        return suspendCoroutine {
            it.resume(true)
        }
    }

}