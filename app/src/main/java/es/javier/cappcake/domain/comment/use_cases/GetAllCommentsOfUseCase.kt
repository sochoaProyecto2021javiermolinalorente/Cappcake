package es.javier.cappcake.domain.comment.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.comment.Comment
import es.javier.cappcake.domain.repositories.CommentsRepository
import javax.inject.Inject

class GetAllCommentsOfUseCase @Inject constructor(private val repository: CommentsRepository) {

    suspend operator fun invoke(recipeId: String) : Response<List<Comment>> = repository.getAllCommentsOf(recipeId)

}