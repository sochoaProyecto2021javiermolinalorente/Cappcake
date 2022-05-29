package es.javier.cappcake.presentation.commentsscreen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.comment.Comment
import es.javier.cappcake.domain.comment.use_cases.AddCommentUseCase
import es.javier.cappcake.domain.comment.use_cases.GetAllCommentsOfUseCase
import es.javier.cappcake.domain.user.User
import es.javier.cappcake.domain.user.use_cases.GetUserProfileUseCase
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CommentsScreenViewModel @Inject constructor(
    private val getAllCommentsOfUseCase: GetAllCommentsOfUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {

    var currentComment by mutableStateOf("")
    var comments = mutableStateListOf<Comment>()
    var showAddCommentAlert = mutableStateOf(false)

    suspend fun getAllCommentsOf(recipeId: String) {
        val response = getAllCommentsOfUseCase(recipeId)

        when (response) {
            is Response.Failiure -> {  }
            is Response.Success -> {
                comments.clear()
                comments.addAll(response.data!!)
            }
        }

    }

    suspend fun addComment(recipeId: String) {
        val response = addCommentUseCase(comment = currentComment, recipeId = recipeId)

        when (response) {
            is Response.Failiure -> { }
            is Response.Success -> {
                currentComment = ""
                getAllCommentsOf(recipeId)
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