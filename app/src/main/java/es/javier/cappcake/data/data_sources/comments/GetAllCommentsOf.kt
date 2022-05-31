package es.javier.cappcake.data.data_sources.comments

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.javier.cappcake.data.entities.FirebaseContracts
import es.javier.cappcake.domain.comment.Comment
import es.javier.cappcake.domain.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GetAllCommentsOf @Inject constructor() {

    private val firestore = Firebase.firestore

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

        val query = if (lastDocumentSnapshot != null) {
            commentsRef.orderBy(FirebaseContracts.COMMENT_TIMESTAMP, Query.Direction.DESCENDING).startAfter(lastDocumentSnapshot).limit(10)
        } else {
            commentsRef.orderBy(FirebaseContracts.COMMENT_TIMESTAMP, Query.Direction.DESCENDING).limit(10)
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
                        continuation.resume(Response.Failiure(data = Pair(emptyList(), ""), message = null))
                    } else {
                        continuation.resume(Response.Success(data = Pair(result, result.last().commentId)))
                    }
                } else {
                    continuation.resume(Response.Failiure(data = Pair(emptyList(), ""), message = null))
                }
            }
        }
    }

}