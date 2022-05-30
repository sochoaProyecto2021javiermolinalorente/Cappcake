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
import es.javier.cappcake.domain.comment.use_cases.UpdateCommentUseCase
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
    private val updateCommentUseCase: UpdateCommentUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : ViewModel() {

    var currentComment by mutableStateOf("")
    var comments = mutableStateListOf<Comment>()
    var lastCommentId: String? by mutableStateOf(null)
    var isRefreshing by mutableStateOf(false)

    var lastFocusCommentId by mutableStateOf("")
    var currentEditingCommentId by mutableStateOf("")

    var screenState: ScreenState by mutableStateOf(ScreenState.LoadingData)
    val showProcessingAlert = mutableStateOf(false)
    val showAddCommentAlert = mutableStateOf(false)
    val showRemoveCommentAlert = mutableStateOf(false)
    val showAddCommentErrorAlert = mutableStateOf(false)
    val showDeleteCommentErrorAlert = mutableStateOf(false)
    val showEditCommentErrorAlert = mutableStateOf(false)

    suspend fun getAllCommentsOf(recipeId: String) {
        val response = getAllCommentsOfUseCase(recipeId, null)

        when (response) {
            is Response.Failiure -> {  }
            is Response.Success -> {
                comments.clear()
                comments.addAll(response.data!!.first)
                lastCommentId = response.data.second
                screenState = ScreenState.DataLoaded
            }
        }

    }

    suspend fun getAllCommentsAgain(recipeId: String) {
        isRefreshing = true
        val response = getAllCommentsOfUseCase(recipeId, null)

        when (response) {
            is Response.Failiure -> { isRefreshing = false }
            is Response.Success -> {
                comments.clear()
                comments.addAll(response.data!!.first)
                lastCommentId = response.data.second
                isRefreshing = false
            }
        }
    }

    suspend fun loadMoreComments(recipeId: String) {
        val response = getAllCommentsOfUseCase(recipeId, lastCommentId)

        when (response) {
            is Response.Failiure -> {  }
            is Response.Success -> {
                comments.addAll(response.data!!.first.toTypedArray())
                lastCommentId = response.data.second
            }
        }
    }

    suspend fun addComment(recipeId: String) {
        showProcessingAlert.value = true
        val response = addCommentUseCase(comment = currentComment, recipeId = recipeId)

        when (response) {
            is Response.Failiure -> {
                showProcessingAlert.value = false
                showAddCommentErrorAlert.value = true
            }
            is Response.Success -> {
                comments.add(0, Comment(
                    UUID.randomUUID().toString(),
                    getCurrentId()!!,
                    recipeId,
                    currentComment
                ))
                currentComment = ""
                showProcessingAlert.value = false
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

    suspend fun deleteComment(recipeId: String) {
        showProcessingAlert.value = true
        val response = deleteCommentUseCase(recipeId, lastFocusCommentId)

        when (response) {
            is Response.Failiure -> {
                showProcessingAlert.value = false
                showDeleteCommentErrorAlert.value = true
            }
            is Response.Success -> {
                val comment = comments.filter { it.commentId == lastFocusCommentId }.first()
                comments.remove(comment)
                lastFocusCommentId = ""
                showProcessingAlert.value = false
            }
        }
    }

    suspend fun updateComment(comment: String, recipeId: String) {
        showProcessingAlert.value = true
        val response = updateCommentUseCase(comment, recipeId, currentEditingCommentId)

        when (response) {
            is Response.Failiure -> {
                showProcessingAlert.value = false
                showEditCommentErrorAlert.value = true
            }
            is Response.Success -> {
                val oldComment = comments.filter { it.commentId == currentEditingCommentId }.first()
                val index = comments.indexOf(oldComment)
                val newComment = Comment(oldComment.commentId, oldComment.userId, oldComment.recipeId, comment )
                comments[index] = newComment
                currentEditingCommentId = ""
                showProcessingAlert.value = false
            }
        }
    }

    fun getCurrentId() : String? = getCurrentUserIdUseCase()

}