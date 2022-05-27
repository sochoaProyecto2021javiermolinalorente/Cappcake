package es.javier.cappcake.presentation.feedscreen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.recipe.Recipe
import es.javier.cappcake.domain.recipe.use_cases.GetRecipesOfUseCase
import es.javier.cappcake.domain.user.User
import es.javier.cappcake.domain.user.use_cases.GetFollowedUserUseCase
import es.javier.cappcake.domain.user.use_cases.GetUserProfileUseCase
import es.javier.cappcake.utils.ScreenState
import javax.inject.Inject

@HiltViewModel
class FeedScreenViewModel @Inject constructor(
    private val getFollowedUserUseCase: GetFollowedUserUseCase,
    private val getRecipesOfUseCase: GetRecipesOfUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {


    var users: List<User>? by mutableStateOf(null)
    var userFilter: String by mutableStateOf("")
    var recipes: SnapshotStateList<Recipe> = mutableStateListOf()
    var lastRecipeId: String? by mutableStateOf(null)
    var screenStatus: ScreenState by mutableStateOf(ScreenState.LoadingData)
    var loadingMoreRecipes by mutableStateOf(false)
    var isRefreshing by mutableStateOf(false)

    suspend fun loadFollowedUsers() {
        val response = getFollowedUserUseCase()

        when (response) {
            is Response.Failiure -> {}
            is Response.Success -> {
                users = response.data
            }
        }
    }


    suspend fun loadRecipesOfFollowers() {
        users?.let {
            if (it.isEmpty()) {
                recipes.clear()
                return
            }

            val ids: Array<String> = if (userFilter.isBlank()) {
                Array(it.size) { position ->
                    it[position].userId
                }
            } else {
                arrayOf(userFilter)
            }


            val response = getRecipesOfUseCase(ids, null)

            when (response) {
                is Response.Failiure -> { }
                is Response.Success -> {
                    recipes.clear()
                    recipes.addAll(response.data!!.first.toTypedArray())
                    lastRecipeId = response.data.second
                    screenStatus = ScreenState.DataLoaded
                }
            }
        }
    }


    suspend fun loadRecipesOfFollowersAgain() {
        isRefreshing = true
        loadFollowedUsers()
        if (users != null) {
            if (users!!.isEmpty()) {
                recipes.clear()
                isRefreshing = false
                return
            }

            val ids: Array<String> = if (userFilter.isBlank()) {
                Array(users!!.size) { position ->
                    users!![position].userId
                }
            } else {
                arrayOf(userFilter)
            }


            val response = getRecipesOfUseCase(ids, null)

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
    }

    suspend fun loadMoreRecipes() {
        users?.let {
            if (it.isEmpty()) {
                recipes.clear()
                return
            }

            loadingMoreRecipes = true
            val ids: Array<String> = if (userFilter.isBlank()) {
                Array(it.size) { position ->
                    it[position].userId
                }
            } else {
                arrayOf(userFilter)
            }


            val response = getRecipesOfUseCase(ids, lastRecipeId)

            when (response) {
                is Response.Failiure -> { loadingMoreRecipes = false }
                is Response.Success -> {
                    recipes.addAll(response.data!!.first.toTypedArray())
                    lastRecipeId = response.data.second
                    loadingMoreRecipes = false
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
