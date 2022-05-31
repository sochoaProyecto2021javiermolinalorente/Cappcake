package es.javier.cappcake.data.data_sources.comments

import es.javier.cappcake.domain.comment.Comment
import es.javier.cappcake.domain.Response
import javax.inject.Inject

class CommentsDataSource @Inject constructor(
    private val getAllCommentsOf: GetAllCommentsOf,
    private val addComment: AddComment,
    private val deleteComment: DeleteComment,
    private val updateComment: UpdateComment
) {

    suspend fun getAllCommentsOf(recipeId: String, lastCommentId: String?) : Response<Pair<List<Comment>, String>> {
        return getAllCommentsOf.getAllCommentsOf(recipeId, lastCommentId)
    }

    suspend fun addComment(comment: String, recipeId: String) : Response<Boolean> {
        return addComment.addComment(comment, recipeId)
    }

    suspend fun deleteComment(recipeId: String, commentId: String) : Response<Boolean> {
        return deleteComment.deleteComment(recipeId, commentId)
    }

    suspend fun updateComment(comment: String, recipeId: String, commentId: String) : Response<Boolean> {
        return updateComment.updateComment(comment, recipeId, commentId)
    }

}