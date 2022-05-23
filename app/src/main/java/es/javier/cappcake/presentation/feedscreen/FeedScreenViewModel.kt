package es.javier.cappcake.presentation.feedscreen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.user.User
import es.javier.cappcake.domain.user.use_cases.GetFollowedUserUseCase
import javax.inject.Inject

@HiltViewModel
class FeedScreenViewModel @Inject constructor(
    private val getFollowedUserUseCase: GetFollowedUserUseCase
) : ViewModel() {


    var users: List<User> by mutableStateOf(emptyList())

    suspend fun loadFollowedUsers() {
        val response = getFollowedUserUseCase()

        when (response) {
            is Response.Failiure -> {}
            is Response.Success -> {
                users = response.data!!
            }
        }
    }


}