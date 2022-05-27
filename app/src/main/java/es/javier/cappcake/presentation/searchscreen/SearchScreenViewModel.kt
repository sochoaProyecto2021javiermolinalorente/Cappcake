package es.javier.cappcake.presentation.searchscreen

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
import es.javier.cappcake.domain.user.User
import es.javier.cappcake.domain.recipe.use_cases.GetAllRecipesUseCase
import es.javier.cappcake.domain.user.use_cases.GetUserProfileUseCase
import es.javier.cappcake.utils.ScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val getAllRecipesUseCase: GetAllRecipesUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {

    var recipes: SnapshotStateList<Recipe> = mutableStateListOf()
    var lastRecipeId: String? by mutableStateOf(null)
    var screenStatus: ScreenState by mutableStateOf(ScreenState.LoadingData)
    var isRefreshing by mutableStateOf(false)

    suspend fun loadAllRecipes() {
        val response = getAllRecipesUseCase(null)

        when (response) {
            is Response.Failiure -> Unit
            is Response.Success -> {
                recipes.clear()
                recipes.addAll(response.data!!.first.toTypedArray())
                lastRecipeId = response.data.second
                screenStatus = ScreenState.DataLoaded
            }
        }
    }

    suspend fun loadRecipesAgain() {
        isRefreshing = true
        val response = getAllRecipesUseCase(null)

        when (response) {
            is Response.Failiure -> { isRefreshing = false }
            is Response.Success -> {
                recipes.clear()
                recipes.addAll(response.data!!.first.toTypedArray())
                lastRecipeId = response.data.second
                isRefreshing = false
            }
        }
    }

    suspend fun loadMoreRecipes() {
        val response = getAllRecipesUseCase(lastRecipeId)

        when (response) {
            is Response.Failiure -> Unit
            is Response.Success -> {
                recipes.addAll(response.data!!.first.toTypedArray())
                lastRecipeId = response.data.second
            }
        }
    }

    suspend fun loadUser(uid: String) : User? {
        val response = getUserProfileUseCase(uid = uid)

        return when (response) {
            is Response.Failiure -> null
            is Response.Success -> response.data!!.first
        }
    }


}