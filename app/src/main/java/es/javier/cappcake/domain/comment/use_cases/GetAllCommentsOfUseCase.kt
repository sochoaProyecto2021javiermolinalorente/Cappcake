package es.javier.cappcake.domain.comment.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.comment.Comment
import es.javier.cappcake.domain.repositories.CommentsRepository
import javax.inject.Inject

class GetAllCommentsOfUseCase @Inject constructor(private val repository: CommentsRepository) {

    /**
     * Method to get an amount of comments that belongs to a group of users
     *
     * @param recipeId The id of the recipe which the comments belongs
     * @param lastCommentId The last comment id of a previous call
     * @return The response with the List of recipes and the last comment id of the list
     */
    suspend operator fun invoke(recipeId: String, lastCommentId: String?) : Response<Pair<List<Comment>, String>> = repository.getAllCommentsOf(recipeId, lastCommentId)

}