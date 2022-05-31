package es.javier.cappcake.presentation.searchscreen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.user.User
import es.javier.cappcake.domain.user.use_cases.GetUsersWhereNameUseCase
import javax.inject.Inject

@HiltViewModel
class SearchUserScreenViewModel @Inject constructor(
    private val getUsersWhereNameUseCase: GetUsersWhereNameUseCase
) : ViewModel() {

    var searchText by mutableStateOf("")
    var users = mutableStateListOf<User>()
    var searching by mutableStateOf(false)


    suspend fun searchUsers() {
        searching = true
        val response = getUsersWhereNameUseCase(searchText)

        when (response) {
            is Response.Failiure -> { searching = false }
            is Response.Success -> {
                users.clear()
                users.addAll(response.data!!.toTypedArray())
                searching = false
            }
        }
    }

}