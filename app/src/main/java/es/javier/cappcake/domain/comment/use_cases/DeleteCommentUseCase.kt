package es.javier.cappcake.domain.comment.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.CommentsRepository
import javax.inject.Inject

class DeleteCommentUseCase @Inject constructor(private val repository: CommentsRepository) {

    /**
     * Method to remove a comment of a recipe
     *
     * @param recipeId The id of the recipe which the comment belongs
     * @param commentId The id of the comment to delete
     * @return The response with the status of the operation
     */
    suspend operator fun invoke(recipeId: String, commentId: String) : Response<Boolean> = repository.removeComment(recipeId, commentId)

}