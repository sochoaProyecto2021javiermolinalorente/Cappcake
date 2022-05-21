package es.javier.cappcake.presentation.profilescreen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import es.javier.cappcake.domain.recipe.Recipe
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.recipe.use_cases.GetRecipeUseCase
import es.javier.cappcake.domain.user.User
import es.javier.cappcake.domain.recipe.use_cases.GetRecipesOfUseCase
import es.javier.cappcake.domain.user.use_cases.GetUserProfileUseCase
import javax.inject.Inject

@HiltViewModel
class ProfileScreenVIewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getRecipesOfUseCase: GetRecipesOfUseCase
) : ViewModel() {

    var user: User? by mutableStateOf(null)
    var recipes: List<Recipe>? by mutableStateOf(null)

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


}