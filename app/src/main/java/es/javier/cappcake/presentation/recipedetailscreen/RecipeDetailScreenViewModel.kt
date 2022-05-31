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
import es.javier.cappcake.domain.user.use_cases.GetCurrentUserIdUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailScreenViewModel @Inject constructor(
    private val getRecipeUseCase: GetRecipeUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : ViewModel() {

    var recipe: Recipe? by mutableStateOf(null)

    fun loadRecipe(recipeId: String) {
        viewModelScope.launch {
            val response = getRecipeUseCase(recipeId = recipeId)

            when (response) {
                is Response.Failiure -> { }
                is Response.Success -> {
                    recipe = response.data
                }
            }
        }
    }

    fun getCurrentId() : String? = getCurrentUserIdUseCase()

}