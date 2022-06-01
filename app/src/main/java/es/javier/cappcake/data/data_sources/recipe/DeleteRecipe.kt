package es.javier.cappcake.data.data_sources.recipe

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.javier.cappcake.data.entities.FirebaseContracts
import es.javier.cappcake.domain.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DeleteRecipe @Inject constructor() {

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth

    suspend fun deleteRecipe(recipeId: String) : Response<Boolean> {

        val recipeRef = firestore.collection(FirebaseContracts.RECIPE_COLLECTION).document(recipeId)
        val userRef = firestore.collection(FirebaseContracts.USER_COLLECTION).document(auth.uid!!)

        return suspendCoroutine { continuation ->
            firestore.runTransaction { transaction ->
                val recipe = transaction.get(recipeRef)
                val likeRecipeRef = recipe.getDocumentReference(FirebaseContracts.RECIPE_LIKES_REF)

                transaction.delete(recipeRef)
                transaction.delete(likeRecipeRef!!)
                transaction.update(userRef, FirebaseContracts.USER_POSTS, FieldValue.increment(-1))


            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(Response.Success(data = true))
                } else {
                    continuation.resume(Response.Failiure(data = false, message = null))
                }
            }
        }

    }

}