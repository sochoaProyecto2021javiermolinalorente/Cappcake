package es.javier.cappcake.presentation.loginscreen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class LoginScreenViewModel @Inject constructor() : ViewModel() {

    val emailField: MutableState<String> = mutableStateOf("user_me@gmail.com")
    val passwordField: MutableState<String> = mutableStateOf("123456")
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
        return suspendCoroutine<Boolean> { continuation ->
            // Validating user buiseness logic

            Firebase.auth.signInWithEmailAndPassword(emailField.value, passwordField.value)
                .addOnSuccessListener { result ->
                    setLoadingStateFalse()
                    continuation.resume(true)
                }
                .addOnFailureListener { exception ->
                    setLoadingStateFalse()
                    continuation.resume(false)
                }
        }
    }

}