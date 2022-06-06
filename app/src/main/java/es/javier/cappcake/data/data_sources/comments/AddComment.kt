package es.javier.cappcake.data.data_sources.comments

import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.javier.cappcake.data.entities.FirebaseContracts
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.activity.ActivityType
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AddComment @Inject constructor() {

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth

    /**
     * Method to add a comment in a subcollection of a recipe in firestore
     *
     * @param comment The body of the comment
     * @param recipeId The id of the recipe to which the comment belongs
     * @return The response with the status of the operation
     */
    suspend fun addComment(comment: String, recipeId: String) : Response<Boolean> {

        // Comment data
        val commentRef = firestore
            .collection(FirebaseContracts.RECIPE_COLLECTION)
            .document(recipeId)
            .collection(FirebaseContracts.COMMENT_COLLECTION)
            .document()

        // Comment data
        val data = hashMapOf(
            FirebaseContracts.COMMENT_RECIPE_ID to recipeId,
            FirebaseContracts.COMMENT_USER_ID to auth.uid!!,
            FirebaseContracts.COMMENT_TIMESTAMP to Timestamp.now(),
            FirebaseContracts.COMMENT_MESSAGE to comment
        )

        return suspendCoroutine { continuation ->

            firestore.runTransaction { transaction ->

                val recipeDocument = transaction.get(firestore.collection(FirebaseContracts.RECIPE_COLLECTION).document(recipeId))

                transaction.set(commentRef, data)

                if (recipeDocument.getString(FirebaseContracts.RECIPE_USER_ID) != auth.uid!!) {
                    // Add activity data
                    val activity = firestore.collection(FirebaseContracts.ACTIVITY_COLLECTION).document()
                    transaction.set(activity, hashMapOf(
                        FirebaseContracts.ACTIVITY_USER_ID to auth.uid!!,
                        FirebaseContracts.ACTIVITY_AFFECTED_USER_ID to recipeDocument.getString(FirebaseContracts.RECIPE_USER_ID),
                        FirebaseContracts.ACTIVITY_TYPE to ActivityType.COMMENT.name,
                        FirebaseContracts.ACTIVITY_TIMESTAMP to Timestamp.now(),
                        FirebaseContracts.ACTIVITY_RECIPE_ID to recipeId
                    ))
                }

            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(Response.Success(data = true))
                } else {
                    continuation.resume(Response.Failiure(data = false, throwable = task.exception))
                }
            }
        }
    }

}