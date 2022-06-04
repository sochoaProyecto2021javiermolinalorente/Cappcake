package es.javier.cappcake.data.data_sources.recipe

import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.javier.cappcake.data.entities.FirebaseContracts
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.activity.ActivityType
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

                if (recipeDocument.getString(FirebaseContracts.RECIPE_USER_ID) != auth.uid!!) {
                    // Add activity data
                    val activity = firestore.collection(FirebaseContracts.ACTIVITY_COLLECTION).document()
                    transaction.set(activity, hashMapOf(
                        FirebaseContracts.ACTIVITY_USER_ID to auth.uid!!,
                        FirebaseContracts.ACTIVITY_AFFECTED_USER_ID to recipeDocument.getString(FirebaseContracts.RECIPE_USER_ID),
                        FirebaseContracts.ACTIVITY_TYPE to ActivityType.LIKE.name,
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