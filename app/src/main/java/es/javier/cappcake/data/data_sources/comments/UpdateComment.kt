package es.javier.cappcake.data.data_sources.comments

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.javier.cappcake.data.entities.FirebaseContracts
import es.javier.cappcake.domain.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UpdateComment @Inject constructor() {

    private val firestore = Firebase.firestore

    suspend fun updateComment(comment: String, recipeId: String, commentId: String) : Response<Boolean> {

        val commentRef = firestore
            .collection(FirebaseContracts.RECIPE_COLLECTION)
            .document(recipeId)
            .collection(FirebaseContracts.COMMENT_COLLECTION)
            .document(commentId)

        return suspendCoroutine { continuation ->
            commentRef.update(FirebaseContracts.COMMENT_MESSAGE, comment).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(Response.Success(data = true))
                } else {
                    continuation.resume(Response.Failiure(data = false, message = null))
                }
            }
        }

    }

}