package es.javier.cappcake.domain.repositories

import es.javier.cappcake.domain.comment.Comment
import es.javier.cappcake.domain.Response

interface CommentsRepository {

    suspend fun getAllCommentsOf(recipeId: String, lastCommentId: String?) : Response<Pair<List<Comment>, String>>
    suspend fun addComment(recipeId: String, comment: String) : Response<Boolean>
    suspend fun removeComment(recipeId: String, commentId: String) : Response<Boolean>
    suspend fun updateComment(comment: String, recipeId: String, commentId: String) : Response<Boolean>

}