package es.javier.cappcake.data.repositories

import es.javier.cappcake.data.data_sources.comments.CommentsDataSource
import es.javier.cappcake.domain.comment.Comment
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.CommentsRepository
import javax.inject.Inject

class ImplCommentsRepository @Inject constructor(private val dataSource: CommentsDataSource) : CommentsRepository {

    override suspend fun getAllCommentsOf(recipeId: String, lastCommentId: String?): Response<Pair<List<Comment>, String>> {
        return dataSource.getAllCommentsOf(recipeId, lastCommentId)
    }

    override suspend fun addComment(recipeId: String, comment: String) : Response<Boolean> {
        return dataSource.addComment(comment, recipeId)
    }

    override suspend fun removeComment(recipeId: String, commentId: String): Response<Boolean> {
        return dataSource.deleteComment(recipeId, commentId)
    }

    override suspend fun updateComment(
        comment: String,
        recipeId: String,
        commentId: String
    ): Response<Boolean> {
        return dataSource.updateComment(comment, recipeId, commentId)
    }
}