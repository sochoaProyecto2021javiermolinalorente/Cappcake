package es.javier.cappcake.presentation.profilescreen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import es.javier.cappcake.domain.Recipe
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.User
import es.javier.cappcake.domain.use_cases.GetRecipesOfUseCase
import es.javier.cappcake.domain.use_cases.GetUserProfileUseCase
import javax.inject.Inject

@HiltViewModel
class ProfileScreenVIewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getRecipesOfUseCase: GetRecipesOfUseCase
) : ViewModel() {

    var username by mutableStateOf("")
    var profileImageUri: String? by mutableStateOf(null)
    var recipes: List<Recipe>? by mutableStateOf(null)

    suspend fun loadProfileImage(uid: String) {

        val response = getUserProfileUseCase.invoke(uid)

        when (response) {
            is Response.Failiure -> {}
            is Response.Success -> {
                val user = response.data
                user?.let {
                    username = it.username
                    profileImageUri = it.profileImage
                }
            }
        }
    }

    suspend fun loadRecipes(uid: String) {
        val response = getRecipesOfUseCase(uid = uid)

        when (response) {
            is Response.Failiure -> {}
            is Response.Success -> {
                recipes = response.data
            }
        }
    }


}