package es.javier.cappcake.domain.comment.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.CommentsRepository
import javax.inject.Inject

class DeleteCommentUseCase @Inject constructor(private val repository: CommentsRepository) {

    suspend operator fun invoke(recipeId: String, commentId: String) : Response<Boolean> = repository.removeComment(recipeId, commentId)

}