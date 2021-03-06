package es.javier.cappcake.presentation.profilescreen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.javier.cappcake.domain.recipe.Recipe
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.recipe.use_cases.DeleteRecipeUseCase
import es.javier.cappcake.domain.recipe.use_cases.GetRecipeUseCase
import es.javier.cappcake.domain.user.User
import es.javier.cappcake.domain.recipe.use_cases.GetRecipesOfUseCase
import es.javier.cappcake.domain.user.use_cases.*
import es.javier.cappcake.utils.ScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getRecipesOfUseCase: GetRecipesOfUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val followUserUseCase: FollowUserUseCase,
    private val unfollowUserUseCase: UnfollowUserUseCase,
    private val getFollowersCountUseCase: GetFollowersCountUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    var user: User? by mutableStateOf(null)
    var followers: Int? by mutableStateOf(null)
    var recipes: SnapshotStateList<Recipe> = mutableStateListOf()
    var lastRecipeId: String? by mutableStateOf(null)
    var selectedRecipe by mutableStateOf("")


    var userFollowed: Boolean by mutableStateOf(false)
    var screenStatus: ScreenState by mutableStateOf(ScreenState.LoadingData)
    var showUnFollowUserAlert = mutableStateOf(false)
    var showDeleteRecipeAlert = mutableStateOf(false)
    var isRefreshing by mutableStateOf(false)

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
        val response = getRecipesOfUseCase(arrayOf(uid), null)

        when (response) {
            is Response.Failiure -> { }
            is Response.Success -> {
                recipes.clear()
                lastRecipeId = if (response.data!!.first.isEmpty()) {
                    null
                } else {
                    recipes.addAll(response.data!!.first.toTypedArray())
                    response.data.second
                }
                screenStatus = ScreenState.DataLoaded
            }
        }
    }

    suspend fun loadRecipesAgain(uid: String) {
        isRefreshing = true
        val response = getRecipesOfUseCase(arrayOf(uid), null)

        when (response) {
            is Response.Failiure -> { isRefreshing = false }
            is Response.Success -> {
                recipes.clear()
                delay(50L)
                lastRecipeId = if (response.data!!.first.isEmpty()) {
                    null
                } else {
                    recipes.addAll(response.data!!.first.toTypedArray())
                    response.data.second
                }
                isRefreshing = false
            }
        }
    }

    suspend fun loadMoreRecipes(uid: String) {

        val response = getRecipesOfUseCase(arrayOf(uid), lastRecipeId)

        when (response) {
            is Response.Failiure -> {
                if (response.throwable is IllegalArgumentException) {
                    lastRecipeId = recipes.last().recipeId
                }
            }
            is Response.Success -> {
                if (response.data!!.first.isNotEmpty()) {
                    recipes.addAll(response.data.first.toTypedArray())
                    lastRecipeId = response.data.second
                }
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

    suspend fun deleteRecipe() {

        val response = deleteRecipeUseCase(selectedRecipe)

        when (response) {
            is Response.Failiure -> {
                selectedRecipe = ""
                showDeleteRecipeAlert.value = false
            }
            is Response.Success -> {
                val deletedRecipe = recipes.filter { it.recipeId == selectedRecipe }.first()
                recipes.remove(deletedRecipe)
                selectedRecipe = ""
                showDeleteRecipeAlert.value = false
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase()
        }
    }

}