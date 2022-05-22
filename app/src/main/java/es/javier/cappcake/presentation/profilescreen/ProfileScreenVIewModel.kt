package es.javier.cappcake.presentation.profilescreen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.javier.cappcake.domain.recipe.Recipe
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.recipe.use_cases.GetRecipeUseCase
import es.javier.cappcake.domain.user.User
import es.javier.cappcake.domain.recipe.use_cases.GetRecipesOfUseCase
import es.javier.cappcake.domain.user.use_cases.FollowUserUseCase
import es.javier.cappcake.domain.user.use_cases.GetCurrentUserIdUseCase
import es.javier.cappcake.domain.user.use_cases.GetUserProfileUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenVIewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getRecipesOfUseCase: GetRecipesOfUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val followUserUseCase: FollowUserUseCase
) : ViewModel() {

    var user: User? by mutableStateOf(null)
    var recipes: List<Recipe>? by mutableStateOf(null)
    var userFollowed: Boolean by mutableStateOf(false)

    suspend fun loadUser(uid: String) {

        val response = getUserProfileUseCase.invoke(uid)

        return when (response) {
            is Response.Failiure -> { }
            is Response.Success -> {
                user = response.data
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

    fun getCurrentUserId() : String? = getCurrentUserIdUseCase()

    fun followUser(uid: String) {
        viewModelScope.launch {
            val response = followUserUseCase(uid)

            when (response) {
                is Response.Failiure -> userFollowed = response.data!!
                is Response.Success -> userFollowed = response.data!!
            }
        }
    }

}