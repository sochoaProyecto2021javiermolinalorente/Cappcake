package es.javier.cappcake.data.data_sources.comments

import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.javier.cappcake.data.entities.FirebaseContracts
import es.javier.cappcake.domain.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AddComment @Inject constructor() {

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth

    suspend fun addComment(comment: String, recipeId: String) : Response<Boolean> {

        val commentRef = firestore
            .collection(FirebaseContracts.RECIPE_COLLECTION)
            .document(recipeId)
            .collection(FirebaseContracts.COMMENT_COLLECTION)
            .document()

        val data = hashMapOf(
            FirebaseContracts.COMMENT_RECIPE_ID to recipeId,
            FirebaseContracts.COMMENT_USER_ID to auth.uid!!,
            FirebaseContracts.COMMENT_TIMESTAMP to Timestamp.now(),
            FirebaseContracts.COMMENT_MESSAGE to comment
        )

        return suspendCoroutine { continuation ->
            commentRef.set(data).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(Response.Success(data = true))
                } else {
                    continuation.resume(Response.Failiure(data = false, message = null))
                }
            }
        }
    }

}