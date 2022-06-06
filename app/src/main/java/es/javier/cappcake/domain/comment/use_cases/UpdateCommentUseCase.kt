package es.javier.cappcake.domain.comment.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.CommentsRepository
import javax.inject.Inject

class UpdateCommentUseCase @Inject constructor(private val repository: CommentsRepository) {

    /**
     * Method to update a comment of a recipe
     *
     * @param comment The new body of the comment
     * @param recipeId The id of the recipe which the comment belongs
     * @param commentId The id of the comment to update
     * @return The response with the status of the operation
     */
    suspend operator fun invoke(comment: String, recipeId: String, commentId: String) : Response<Boolean> = repository.updateComment(comment, recipeId, commentId)

}