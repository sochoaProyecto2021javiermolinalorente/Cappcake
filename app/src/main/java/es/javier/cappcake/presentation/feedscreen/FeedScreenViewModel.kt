package es.javier.cappcake.presentation.feedscreen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.recipe.Recipe
import es.javier.cappcake.domain.recipe.use_cases.GetRecipesOfUseCase
import es.javier.cappcake.domain.user.User
import es.javier.cappcake.domain.user.use_cases.GetFollowedUserUseCase
import es.javier.cappcake.domain.user.use_cases.GetUserProfileUseCase
import javax.inject.Inject

@HiltViewModel
class FeedScreenViewModel @Inject constructor(
    private val getFollowedUserUseCase: GetFollowedUserUseCase,
    private val getRecipesOfUseCase: GetRecipesOfUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {


    var users: List<User>? by mutableStateOf(null)
    var userFilter: String by mutableStateOf("")
    var recipes: List<Recipe>? by mutableStateOf(null)

    suspend fun loadFollowedUsers() {
        val response = getFollowedUserUseCase()

        when (response) {
            is Response.Failiure -> {}
            is Response.Success -> {
                users = response.data!!
            }
        }
    }


    suspend fun loadRecipesOfFollowers() {
        users?.let {
            if (it.isEmpty()) {
                recipes = emptyList()
                return
            }

            val ids: Array<String> = if (userFilter.isBlank()) {
                Array(it.size) { position ->
                    it[position].userId
                }
            } else {
                arrayOf(userFilter)
            }


            val response = getRecipesOfUseCase(ids)

            when (response) {
                is Response.Failiure -> { }
                is Response.Success -> {
                    recipes = response.data
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

}