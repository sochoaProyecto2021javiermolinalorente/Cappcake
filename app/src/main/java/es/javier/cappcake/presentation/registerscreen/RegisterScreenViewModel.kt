package es.javier.cappcake.presentation.registerscreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import dagger.hilt.android.lifecycle.HiltViewModel
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.use_cases.RegisterUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterScreenViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase) : ViewModel() {

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

    suspend fun createUser(): Boolean {
        creatingUser.value = true

        val response = registerUserUseCase(username = usernameField, email = emailField, password = passwordField, image = null)

        return when (response) {
            is Response.Success -> {
                creatingUser.value = false
                response.data!!
            }
            is Response.Failiure -> {
                creatingUser.value = false
                response.data!!
            }
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