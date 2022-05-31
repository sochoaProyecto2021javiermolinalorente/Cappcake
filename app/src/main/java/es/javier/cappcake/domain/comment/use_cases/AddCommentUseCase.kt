package es.javier.cappcake.domain.comment.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.CommentsRepository
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(private val repository: CommentsRepository) {

    suspend operator fun invoke(comment: String, recipeId: String) : Response<Boolean> = repository.addComment(recipeId, comment)

}