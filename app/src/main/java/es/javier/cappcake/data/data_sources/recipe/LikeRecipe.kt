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

class LikeRecipe @Inject constructor() {

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth

    suspend fun likeRecipe(recipeId: String) : Response<Boolean> {

      val recipeRef = firestore.collection(FirebaseContracts.RECIPE_COLLECTION).document(recipeId)


        return suspendCoroutine { continuation ->

            firestore.runTransaction { transaction ->

                val recipeDocument = transaction.get(recipeRef)

                val ref = recipeDocument.getDocumentReference(FirebaseContracts.RECIPE_LIKES_REF)

                transaction
                    .update(
                        ref!!,
                        FirebaseContracts.LIKE_USERS,
                        FieldValue.arrayUnion(auth.uid!!)
                    )

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