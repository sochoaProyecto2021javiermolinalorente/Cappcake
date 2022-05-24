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
import es.javier.cappcake.domain.user.use_cases.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenVIewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getRecipesOfUseCase: GetRecipesOfUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val followUserUseCase: FollowUserUseCase,
    private val unfollowUserUseCase: UnfollowUserUseCase,
    private val getFollowersCountUseCase: GetFollowersCountUseCase
) : ViewModel() {

    var user: User? by mutableStateOf(null)
    var followers: Int? by mutableStateOf(null)
    var recipes: List<Recipe>? by mutableStateOf(null)
    var userFollowed: Boolean by mutableStateOf(false)
    var showUnFollowUserAlert = mutableStateOf(false)

    suspend fun loadUser(uid: String) {

        val response = getUserProfileUseCase.invoke(uid)

        when (response) {
            is Response.Failiure -> { }
            is Response.Success -> {
                user = response.data!!.first
                userFollowed = response.data.second
            }
        }
    }

    suspend fun getFollowersCount(uid: String) {
        val response = getFollowersCountUseCase(uid = uid)

        when (response) {
            is Response.Failiure -> { }
            is Response.Success -> {
                followers = response.data
            }
        }
    }

    suspend fun loadRecipes(uid: String) {
        val response = getRecipesOfUseCase(arrayOf(uid))

        when (response) {
            is Response.Failiure -> {}
            is Response.Success -> {
                recipes = response.data
            }
        }
    }

    fun getCurrentUserId() : String? = getCurrentUserIdUseCase()

    suspend fun followUser(uid: String) {
        val response = followUserUseCase(uid)

        when (response) {
            is Response.Failiure -> userFollowed = response.data!!
            is Response.Success -> userFollowed = response.data!!
        }
    }
    
    suspend fun unfollowUser(uid: String) {
        val response = unfollowUserUseCase(uid)

        when (response) {
            is Response.Failiure -> {  }
            is Response.Success -> userFollowed = false
        }
    }

}