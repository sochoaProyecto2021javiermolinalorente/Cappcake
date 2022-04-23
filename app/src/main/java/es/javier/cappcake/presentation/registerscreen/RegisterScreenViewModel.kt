package es.javier.cappcake.presentation.registerscreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RegisterScreenViewModel : ViewModel() {

    // Fields
    var usernameField: String by mutableStateOf("")
    var emailField: String by mutableStateOf("")
    var passwordField: String by mutableStateOf("")
    var repeatPasswordField: String by mutableStateOf("")
    var creatingUser = MutableStateFlow(false)
        private set

    // Availability
    var userFieldEnabled: Boolean by mutableStateOf(true)
        private set
    var emailFieldEnabled: Boolean by mutableStateOf(true)
        private set
    var passwordFieldEnabled: Boolean by mutableStateOf(true)
        private set
    var repeatPasswordFieldEnabled: Boolean by mutableStateOf(true)
        private set
    var createUserButtonEnabled: Boolean by mutableStateOf(true)
        private set

    fun createUser() {
        creatingUser.value = true
        viewModelScope.launch {
            delay(2_000)
            creatingUser.value = false
        }
    }

    fun setScreenState() {
        if (creatingUser.value) {
            inactiveScreenStatus()
        } else {
            activeScreenStatus()
        }
    }

    private fun inactiveScreenStatus() {
        userFieldEnabled = false
        emailFieldEnabled = false
        passwordFieldEnabled = false
        repeatPasswordFieldEnabled = false
        createUserButtonEnabled = false
    }

    private fun activeScreenStatus() {
        userFieldEnabled = true
        emailFieldEnabled = true
        passwordFieldEnabled = true
        repeatPasswordFieldEnabled = true
        createUserButtonEnabled = true
    }

}