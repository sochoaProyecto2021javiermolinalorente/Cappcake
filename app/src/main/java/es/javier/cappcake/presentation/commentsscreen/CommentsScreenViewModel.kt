package es.javier.cappcake.presentation.commentsscreen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import es.javier.cappcake.domain.Comment
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CommentsScreenViewModel @Inject constructor() : ViewModel() {

    var currentComment by mutableStateOf("")
    var comments = mutableStateListOf<Comment>()
    var showAddCommentAlert = mutableStateOf(false)


    suspend fun addComment() {
        val comment = Comment(
            commentId = UUID.randomUUID().toString(),
            userId = "",
            recipeId = "",
            commentDate = Date().time,
            comment = currentComment
        )

        currentComment = ""

        comments.add(comment)
    }

}