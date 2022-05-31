package es.javier.cappcake.domain.comment.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.CommentsRepository
import javax.inject.Inject

class UpdateCommentUseCase @Inject constructor(private val repository: CommentsRepository) {

    suspend operator fun invoke(comment: String, recipeId: String, commentId: String) : Response<Boolean> = repository.updateComment(comment, recipeId, commentId)

}