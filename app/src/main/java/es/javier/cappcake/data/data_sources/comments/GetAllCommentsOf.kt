package es.javier.cappcake.data.data_sources.comments

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.javier.cappcake.data.entities.FirebaseContracts
import es.javier.cappcake.domain.comment.Comment
import es.javier.cappcake.domain.Response
import java.lang.IllegalArgumentException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GetAllCommentsOf @Inject constructor() {

    private val firestore = Firebase.firestore

    /**
     * Method to get an amount of comments of a recipe from last comment collected in firestore.
     *
     * @param recipeId The id of the  recipe to which the comments belongs
     * @param lastCommentId The last comment id collected by a previous call
     * @return The response with the List of comments and the last comment id of that list
     */
    suspend fun getAllCommentsOf(recipeId: String, lastCommentId: String?) : Response<Pair<List<Comment>, String>> {

        val commentsRef = firestore.collection(FirebaseContracts.RECIPE_COLLECTION)
            .document(recipeId)
            .collection(FirebaseContracts.COMMENT_COLLECTION)

        val lastDocumentSnapshot: DocumentSnapshot? = if (lastCommentId != null) {
            suspendCoroutine { continuation ->
                firestore.collection(FirebaseContracts.RECIPE_COLLECTION)
                    .document(recipeId)
                    .collection(FirebaseContracts.COMMENT_COLLECTION)
                    .document(lastCommentId)
                    .get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(task.result)
                        } else {
                            continuation.resume(null)
                        }
                    }
            }
        } else {
            null
        }

        val query = try {
            if (lastDocumentSnapshot != null) {
                commentsRef.orderBy(FirebaseContracts.COMMENT_TIMESTAMP, Query.Direction.DESCENDING).startAfter(lastDocumentSnapshot).limit(10)
            } else {
                commentsRef.orderBy(FirebaseContracts.COMMENT_TIMESTAMP, Query.Direction.DESCENDING).limit(10)
            }
        } catch(ex: IllegalArgumentException) {
            return Response.Failiure(data = Pair(emptyList(), ""), throwable = ex)
        }

        return suspendCoroutine { continuation ->
            query.get().addOnCompleteListener { task ->

                val result = task.result.map { document ->
                    val commentId = document.id
                    val commentUserId = document.getString(FirebaseContracts.COMMENT_USER_ID) ?: "Unknown"
                    val commentRecipeId = document.getString(FirebaseContracts.COMMENT_RECIPE_ID) ?: "Unknown"
                    val commentMessage = document.getString(FirebaseContracts.COMMENT_MESSAGE) ?: "Unknown"
                    return@map Comment(
                        commentId = commentId,
                        userId = commentUserId,
                        recipeId = commentRecipeId,
                        comment = commentMessage
                    )
                }

                if (task.isSuccessful) {
                    if (result.isEmpty()) {
                        continuation.resume(Response.Success(data = Pair(emptyList(), "")))
                    } else {
                        continuation.resume(Response.Success(data = Pair(result, result.last().commentId)))
                    }
                } else {
                    continuation.resume(Response.Failiure(data = Pair(emptyList(), ""), throwable = task.exception))
                }
            }
        }
    }

}