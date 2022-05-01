package es.javier.cappcake.presentation.registerscreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.javier.cappcake.domain.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class RegisterScreenViewModel @Inject constructor() : ViewModel() {

    // Fields
    var usernameField: String by mutableStateOf("")
    var emailField: String by mutableStateOf("user_me@gmail.com")
    var passwordField: String by mutableStateOf("123456 ")
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

        val db = Firebase.firestore

        val user = User("qwert", "UserMeName", emailField)

        val userRegistered = suspendCoroutine<Boolean> { continuation ->
            Firebase.auth.createUserWithEmailAndPassword(emailField, passwordField)
                .addOnSuccessListener { authResult ->
                    creatingUser.value = false
                    continuation.resume(authResult.user != null)
                }
                .addOnFailureListener { exception ->
                    creatingUser.value = false
                    continuation.resume(false)
                }
        }

        return if (userRegistered) {
            suspendCoroutine { continuation ->
                db.collection("users").document(user.userId).set(user)
                    .addOnSuccessListener {
                        continuation.resume(true)
                    }
                    .addOnFailureListener { exception ->
                        continuation.resume(false)
                    }
            }
        } else {
            false
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