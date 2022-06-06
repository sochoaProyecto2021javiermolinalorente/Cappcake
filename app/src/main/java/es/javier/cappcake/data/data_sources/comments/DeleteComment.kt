package es.javier.cappcake.data.data_sources.comments

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.javier.cappcake.data.entities.FirebaseContracts
import es.javier.cappcake.domain.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DeleteComment @Inject constructor() {

    private val firestore = Firebase.firestore

    /**
     * Method to delete a comment document of the subcollection of a recipe in firestore
     *
     * @param recipeId  The id of the recipe to which the comment belongs
     * @param commentId The id of the comment which is gonna be deleted
     * @return The response with the status of the operation
     */
    suspend fun deleteComment(recipeId: String, commentId: String) : Response<Boolean> {

        val query = firestore
            .collection(FirebaseContracts.RECIPE_COLLECTION)
            .document(recipeId)
            .collection(FirebaseContracts.COMMENT_COLLECTION)
            .document(commentId)
            .delete()

        return suspendCoroutine { continuation ->
            query.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(Response.Success(data = true))
                } else {
                    val exception = task.exception
                    continuation.resume(Response.Failiure(data = false, throwable = task.exception))
                }
            }
        }

    }

}