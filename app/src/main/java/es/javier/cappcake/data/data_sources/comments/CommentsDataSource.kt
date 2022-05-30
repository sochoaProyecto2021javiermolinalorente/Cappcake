package es.javier.cappcake.data.data_sources.comments

import es.javier.cappcake.domain.comment.Comment
import es.javier.cappcake.domain.Response
import javax.inject.Inject

class CommentsDataSource @Inject constructor(
    private val getAllCommentsOf: GetAllCommentsOf,
    private val addComment: AddComment,
    private val deleteComment: DeleteComment
) {

    suspend fun getAllCommentsOf(recipeId: String) : Response<List<Comment>> {
        return getAllCommentsOf.getAllCommentsOf(recipeId)
    }

    suspend fun addComment(comment: String, recipeId: String) : Response<Boolean> {
        return addComment.addComment(comment, recipeId)
    }

    suspend fun deleteComment(recipeId: String, commentId: String) : Response<Boolean> {
        return deleteComment.deleteComment(recipeId, commentId)
    }

}