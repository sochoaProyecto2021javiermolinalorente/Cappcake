package es.javier.cappcake.presentation.likedrecipesscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.recipe.Recipe
import es.javier.cappcake.domain.recipe.use_cases.GetLikedRecipesUseCase
import es.javier.cappcake.domain.user.User
import es.javier.cappcake.domain.user.use_cases.GetCurrentUserIdUseCase
import es.javier.cappcake.domain.user.use_cases.GetUserProfileUseCase
import es.javier.cappcake.utils.ScreenState
import java.lang.IllegalArgumentException
import javax.inject.Inject

@HiltViewModel
class LikedRecipesScreenViewModel @Inject constructor(
    private val getLikedRecipesUseCase: GetLikedRecipesUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : ViewModel() {

    var recipes: SnapshotStateList<Recipe> = mutableStateListOf()
    var lastRecipeId: String? by mutableStateOf(null)
    var refreshing by mutableStateOf(false)
    var screenState: ScreenState by mutableStateOf(ScreenState.LoadingData)

    suspend fun loadRecipes() {
        val response = getLikedRecipesUseCase(null)

        when (response) {
            is Response.Failiure -> { }
            is Response.Success -> {
                recipes.clear()
                lastRecipeId = if (response.data!!.first.isEmpty()) {
                    null
                } else {
                    recipes.addAll(response.data.first.toTypedArray())
                    response.data.second
                }
                screenState = ScreenState.DataLoaded
            }
        }

    }

    suspend fun loadRecipesAgain() {
        refreshing = true
        val response = getLikedRecipesUseCase(null)

        when (response) {
            is Response.Failiure -> {
                recipes.clear()
                refreshing = false
            }
            is Response.Success -> {
                recipes.clear()
                lastRecipeId = if (response.data!!.first.isEmpty()) {
                    null
                } else {
                    recipes.addAll(response.data.first.toTypedArray())
                    response.data.second
                }
                refreshing = false
            }
        }
    }

    suspend fun loadMoreRecipes() {
        val response = getLikedRecipesUseCase(lastRecipeId)

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

    suspend fun loadUser(uid: String) : User? {
        val response = getUserProfileUseCase(uid = uid)

        return when (response) {
            is Response.Failiure -> null
            is Response.Success -> response.data?.first
        }
    }

    fun getCurrentId() : String? = getCurrentUserIdUseCase()

}