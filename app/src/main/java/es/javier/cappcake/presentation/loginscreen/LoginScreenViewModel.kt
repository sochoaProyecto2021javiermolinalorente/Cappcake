package es.javier.cappcake.presentation.loginscreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.user.use_cases.AuthenticateUserUseCase

@HiltViewModel
class LoginScreenViewModel @Inject constructor(val autenticateUserUseCase: AuthenticateUserUseCase) : ViewModel() {

    val emailField: MutableState<String> = mutableStateOf("")
    val passwordField: MutableState<String> = mutableStateOf("")
    val loginButtonEnabled: MutableState<Boolean> = mutableStateOf(false)
    val validatingUser: MutableState<Boolean> = mutableStateOf(false)
    val emailFieldEnabled = mutableStateOf(true)
    val passwordFieldEnabled = mutableStateOf(true)

    private fun setLoadingStateTrue() {
        validatingUser.value = true
        emailFieldEnabled.value = false
        passwordFieldEnabled.value = false
        loginButtonEnabled.value = false
    }

    private fun setLoadingStateFalse() {
        validatingUser.value = false
        emailFieldEnabled.value = true
        passwordFieldEnabled.value = true
        loginButtonEnabled.value = true
    }

    fun shouldLoginButtonEnabled() {
        loginButtonEnabled.value = emailField.value.isNotBlank() && passwordField.value.isNotBlank()
    }


    suspend fun validateUser() : Boolean {
        setLoadingStateTrue()
        return when (val response = autenticateUserUseCase(emailField.value, passwordField.value)) {
            is Response.Failiure -> {
                setLoadingStateFalse()
                response.data!!
            }
            is Response.Success ->  {
                setLoadingStateFalse()
                response.data!!
            }
        }
    }

}