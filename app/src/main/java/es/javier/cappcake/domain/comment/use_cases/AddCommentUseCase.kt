package es.javier.cappcake.domain.comment.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.CommentsRepository
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(private val repository: CommentsRepository) {

    /**
     * Method to add a comment in a recipe
     *
     * @param recipeId The id of the recipe which the comment will belong
     * @param comment The body of the comment
     * @return The response with status of the operation
     */
    suspend operator fun invoke(comment: String, recipeId: String) : Response<Boolean> = repository.addComment(recipeId, comment)

}