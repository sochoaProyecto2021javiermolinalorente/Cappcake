package es.javier.cappcake.presentation.searchscreen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.javier.cappcake.domain.recipe.Recipe
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.user.User
import es.javier.cappcake.domain.recipe.use_cases.GetAllRecipesUseCase
import es.javier.cappcake.domain.user.use_cases.GetUserProfileUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val getAllRecipesUseCase: GetAllRecipesUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {

    var recipes: List<Recipe> by mutableStateOf(emptyList())

    fun loadAllRecipes() {
        viewModelScope.launch {
            val response = getAllRecipesUseCase()

            when (response) {
                is Response.Failiure -> Unit
                is Response.Success -> {
                    recipes = response.data!!
                }
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