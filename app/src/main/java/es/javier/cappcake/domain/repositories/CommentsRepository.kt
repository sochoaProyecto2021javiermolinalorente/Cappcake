package es.javier.cappcake.domain.repositories

import es.javier.cappcake.domain.comment.Comment
import es.javier.cappcake.domain.Response

interface CommentsRepository {

    suspend fun getAllCommentsOf(recipeId: String) : Response<List<Comment>>
    suspend fun addComment(recipeId: String, comment: String) : Response<Boolean>
    suspend fun removeComment(recipeId: String, commentId: String) : Response<Boolean>

}