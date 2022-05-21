package es.javier.cappcake.presentation.registerscreen

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.user.use_cases.RegisterUserUseCase
import es.javier.cappcake.utils.ImageCompressor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterScreenViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val compressor: ImageCompressor
) : ViewModel() {

    // Fields
    var profileImageUri: Uri? by mutableStateOf(null)
        private set
    var profileImage: Bitmap? by mutableStateOf(null)
    private set
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
    var createUserButtonEnabled: Boolean by mutableStateOf(false)
        private set

    var notEqualPasswordError: Boolean by mutableStateOf(false)

    val showStoragePermissionAlert = mutableStateOf(false)
    val showUserNotCreatedAlert = mutableStateOf(false)

    suspend fun createUser(): Boolean {
        creatingUser.value = true

        if (notEqualPasswordError) {
            creatingUser.value = false
            return false
        }

        val response = registerUserUseCase(username = usernameField, email = emailField, password = passwordField, image = profileImageUri)

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

    fun updateProfileImage(imageUri: Uri) {
        viewModelScope.launch {
            profileImageUri = imageUri
            profileImage = compressor.comporessBitmap(ImageCompressor.LOW_QUALITY, imageUri)
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
        checkAllFieldsAreFilled()
    }


    // Check funs

    fun checkAllFieldsAreFilled() {
        createUserButtonEnabled = usernameField.isNotBlank()
                && emailField.isNotBlank()
                && passwordField.isNotBlank()
                && repeatPasswordField.isNotBlank()
    }

    fun checkPasswordsAreEqual() {
        notEqualPasswordError = passwordField != repeatPasswordField
    }

}