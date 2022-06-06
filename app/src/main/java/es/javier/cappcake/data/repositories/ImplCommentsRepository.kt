package es.javier.cappcake.data.repositories

import es.javier.cappcake.data.data_sources.comments.CommentsDataSource
import es.javier.cappcake.domain.comment.Comment
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.CommentsRepository
import javax.inject.Inject

class ImplCommentsRepository @Inject constructor(private val dataSource: CommentsDataSource) : CommentsRepository {

    /**
     * Method to get an amount of comments that belongs to a group of users
     *
     * @param recipeId The id of the recipe which the comments belongs
     * @param lastCommentId The last comment id of a previous call
     * @return The response with the List of recipes and the last comment id of the list
     */
    override suspend fun getAllCommentsOf(recipeId: String, lastCommentId: String?): Response<Pair<List<Comment>, String>> {
        return dataSource.getAllCommentsOf(recipeId, lastCommentId)
    }

    /**
     * Method to add a comment in a recipe
     *
     * @param recipeId The id of the recipe which the comment will belong
     * @param comment The body of the comment
     * @return The response with status of the operation
     */
    override suspend fun addComment(recipeId: String, comment: String) : Response<Boolean> {
        return dataSource.addComment(comment, recipeId)
    }

    /**
     * Method to remove a comment of a recipe
     *
     * @param recipeId The id of the recipe which the comment belongs
     * @param commentId The id of the comment to delete
     * @return The response with the status of the operation
     */
    override suspend fun removeComment(recipeId: String, commentId: String): Response<Boolean> {
        return dataSource.deleteComment(recipeId, commentId)
    }

    /**
     * Method to update a comment of a recipe
     *
     * @param comment The new body of the comment
     * @param recipeId The id of the recipe which the comment belongs
     * @param commentId The id of the comment to update
     * @return The response with the status of the operation
     */
    override suspend fun updateComment(
        comment: String,
        recipeId: String,
        commentId: String
    ): Response<Boolean> {
        return dataSource.updateComment(comment, recipeId, commentId)
    }
}