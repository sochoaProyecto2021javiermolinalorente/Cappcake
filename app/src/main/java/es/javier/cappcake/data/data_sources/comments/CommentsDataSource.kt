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

    /**
     * Method to get an amount of comments of a recipe from last comment collected in firestore.
     *
     * @param recipeId The id of the  recipe to which the comments belongs
     * @param lastCommentId The last comment id collected by a previous call
     * @return The response with the List of comments and the last comment id of that list
     */
    suspend fun getAllCommentsOf(recipeId: String, lastCommentId: String?) : Response<Pair<List<Comment>, String>> {
        return getAllCommentsOf.getAllCommentsOf(recipeId, lastCommentId)
    }

    /**
     * Method to add a comment in a subcollection of a recipe in firestore
     *
     * @param comment The body of the comment
     * @param recipeId The id of the recipe to which the comment belongs
     * @return The response with the status of the operation
     */
    suspend fun addComment(comment: String, recipeId: String) : Response<Boolean> {
        return addComment.addComment(comment, recipeId)
    }

    /**
     * Method to delete a comment document of the subcollection of a recipe in firestore
     *
     * @param recipeId  The id of the recipe to which the comment belongs
     * @param commentId The id of the comment which is gonna be deleted
     * @return The response with the status of the operation
     */
    suspend fun deleteComment(recipeId: String, commentId: String) : Response<Boolean> {
        return deleteComment.deleteComment(recipeId, commentId)
    }

    /**
     * Method to update a comment in firestore
     *
     * @param comment The new body of the comment
     * @param recipeId The id of the recipe to which the comment belongs
     * @param commentId The id of the comment which is gonna be updated
     * @return The response with the status of the operation
     */
    suspend fun updateComment(comment: String, recipeId: String, commentId: String) : Response<Boolean> {
        return updateComment.updateComment(comment, recipeId, commentId)
    }

}