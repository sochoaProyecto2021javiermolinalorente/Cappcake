package es.javier.cappcake.presentation.recipedetailscreen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.recipe.Recipe
import es.javier.cappcake.domain.recipe.use_cases.GetRecipeUseCase
import es.javier.cappcake.domain.recipe.use_cases.LikeRecipeUseCase
import es.javier.cappcake.domain.recipe.use_cases.UnlikeRecipeUseCase
import es.javier.cappcake.domain.user.use_cases.GetCurrentUserIdUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailScreenViewModel @Inject constructor(
    private val getRecipeUseCase: GetRecipeUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val likeRecipeUseCase: LikeRecipeUseCase,
    private val unlikeRecipeUseCase: UnlikeRecipeUseCase
) : ViewModel() {

    var recipe: Recipe? by mutableStateOf(null)
    var recipeLiked by mutableStateOf(false)

    suspend fun loadRecipe(recipeId: String) {
        val response = getRecipeUseCase(recipeId = recipeId)

        when (response) {
            is Response.Failiure -> { }
            is Response.Success -> {
                recipe = response.data
            }
        }

    }

    suspend fun likeRecipe(recipeId: String) {
        val response = likeRecipeUseCase(recipeId)

        when (response) {
            is Response.Failiure -> { }
            is Response.Success -> {
                recipeLiked = true
            }
        }
    }

    suspend fun unlikeRecipe(recipeId: String) {
        val response = unlikeRecipeUseCase(recipeId)

        when (response) {
            is Response.Failiure -> { }
            is Response.Success -> {
                recipeLiked = false
            }
        }
    }

    fun getCurrentId() : String? = getCurrentUserIdUseCase()

}