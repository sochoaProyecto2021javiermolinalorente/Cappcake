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
import es.javier.cappcake.domain.comment.use_cases.DeleteCommentUseCase
import es.javier.cappcake.domain.comment.use_cases.GetAllCommentsOfUseCase
import es.javier.cappcake.domain.user.User
import es.javier.cappcake.domain.user.use_cases.GetCurrentUserIdUseCase
import es.javier.cappcake.domain.user.use_cases.GetUserProfileUseCase
import es.javier.cappcake.utils.ScreenState
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CommentsScreenViewModel @Inject constructor(
    private val getAllCommentsOfUseCase: GetAllCommentsOfUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : ViewModel() {

    var currentComment by mutableStateOf("")
    var comments = mutableStateListOf<Comment>()
    var isRefreshing by mutableStateOf(false)

    var deletedCommentId by mutableStateOf("")

    var screenState: ScreenState by mutableStateOf(ScreenState.LoadingData)
    var showAddCommentAlert = mutableStateOf(false)
    var showRemoveCommentAlert = mutableStateOf(false)

    suspend fun getAllCommentsOf(recipeId: String) {
        val response = getAllCommentsOfUseCase(recipeId)

        when (response) {
            is Response.Failiure -> {  }
            is Response.Success -> {
                comments.clear()
                comments.addAll(response.data!!)
                screenState = ScreenState.DataLoaded
            }
        }

    }

    suspend fun getAllCommentsAgain(recipeId: String) {
        isRefreshing = true
        val response = getAllCommentsOfUseCase(recipeId)

        when (response) {
            is Response.Failiure -> { isRefreshing = false }
            is Response.Success -> {
                comments.clear()
                comments.addAll(response.data!!)
                isRefreshing = false
            }
        }
    }

    suspend fun addComment(recipeId: String) {
        val response = addCommentUseCase(comment = currentComment, recipeId = recipeId)

        when (response) {
            is Response.Failiure -> { }
            is Response.Success -> {
                comments.add(0, Comment(
                    UUID.randomUUID().toString(),
                    getCurrentId()!!,
                    recipeId,
                    currentComment
                ))
                currentComment = ""
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

    suspend fun deleteComment(recipeId: String, commentId: String) {
        val response = deleteCommentUseCase(recipeId, commentId)

        when (response) {
            is Response.Failiure -> { }
            is Response.Success -> {
                val comment = comments.filter { it.commentId == commentId }.first()
                comments.remove(comment)
                deletedCommentId = ""
            }
        }
    }

    fun getCurrentId() : String? = getCurrentUserIdUseCase()

}