package es.javier.cappcake.presentation.editprofilescreen

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.user.User
import es.javier.cappcake.domain.user.use_cases.GetCurrentUserIdUseCase
import es.javier.cappcake.domain.user.use_cases.GetUserProfileUseCase
import es.javier.cappcake.domain.user.use_cases.LoadProfileImageUseCase
import es.javier.cappcake.domain.user.use_cases.UpdateProfileUseCase
import es.javier.cappcake.utils.ImageCompressor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileScreenViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val loadProfileImageUseCase: LoadProfileImageUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val compressor: ImageCompressor
) : ViewModel() {

    var user: User? by mutableStateOf(null)
    var username by mutableStateOf("")
    var profileImageUri: Uri? by mutableStateOf(null)
    var profileImage: Bitmap? by mutableStateOf(null)

    private var _screenState: MutableStateFlow<EditProfileScreenState> = MutableStateFlow(EditProfileScreenState.LoadBaseDate)
    val screenState : StateFlow<EditProfileScreenState> get() = _screenState
    var showImageOptionAlert = mutableStateOf(false)
    var showCanNotUpdateAlert = mutableStateOf(false)

    suspend fun loadUser() {
        getCurrentUserIdUseCase()?.let { user ->
            val response = getUserProfileUseCase(user)

            when (response) {
                is Response.Failiure -> { }
                is Response.Success -> {
                    this.user = response.data!!.first
                    username = response.data.first.username
                    if (this.user?.profileImage != null)
                        profileImageUri = Uri.parse(response.data.first.profileImage)
                    _screenState.emit(EditProfileScreenState.BaseData)
                }
            }
        }
    }

    fun loadProfileImage() {
        viewModelScope.launch {
            profileImageUri?.let {
                profileImage = loadProfileImageUseCase(it.toString()).data
            }
        }
    }

    suspend fun setProfileImage(uri: Uri?) {
        profileImage = if (uri != null) {
            compressor.comporessBitmap(ImageCompressor.MAX_QUALITY, uri)
        } else {
            null
        }
        profileImageUri = uri
        if (user?.username == username && user?.profileImage == profileImageUri?.toString()) {
            _screenState.emit(EditProfileScreenState.BaseData)
        } else {
            _screenState.emit(EditProfileScreenState.DataChanged)
        }
    }

    suspend fun setUsername(newUsername: String) {
        username = newUsername
        if (user?.username == username && user?.profileImage == profileImageUri?.toString()) {
            _screenState.emit(EditProfileScreenState.BaseData)
        } else {
            _screenState.emit(EditProfileScreenState.DataChanged)
        }

    }

    suspend fun updateUserProfile() {
        _screenState.emit(EditProfileScreenState.Updating)
        val response = updateProfileUseCase(username, profileImageUri)

        when (response) {
            is Response.Failiure -> {
                showCanNotUpdateAlert.value = true
                _screenState.emit(EditProfileScreenState.DataChanged)
            }
            is Response.Success -> {
                _screenState.emit(EditProfileScreenState.Updated)
            }
        }
    }

}

sealed class EditProfileScreenState {
    object LoadBaseDate : EditProfileScreenState()
    object BaseData : EditProfileScreenState()
    object DataChanged : EditProfileScreenState()
    object Updating : EditProfileScreenState()
    object Updated : EditProfileScreenState()
}