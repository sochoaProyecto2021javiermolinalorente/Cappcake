package es.javier.cappcake.data.data_sources.recipe

import android.net.Uri
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import es.javier.cappcake.data.data_sources.ImageUploader
import es.javier.cappcake.data.entities.FirebaseContracts
import es.javier.cappcake.domain.Ingredient
import es.javier.cappcake.domain.Response
import es.javier.cappcake.utils.ImageCompressor
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UploadRecipe @Inject constructor(
    private val imageUploader: ImageUploader
) {

    private val auth = Firebase.auth
    private val firestore = Firebase.firestore
    private val storage = Firebase.storage

    /**
     * Method to upload a recipe in firestore
     *
     * @param recipeName The recipe name of the recipe
     * @param recipeImageUri The recipe image of the recipe
     * @param recipeProcess The recipe process of the recipe
     * @param ingredients The list of ingredients of the recipe
     * @return The response with the status of the operation
     */
    suspend fun uploadRecipe(recipeName: String, recipeImageUri: Uri?, recipeProcess: String, ingredients: List<Ingredient>) : Response<Boolean> {

        val recipeDocumentRef = firestore.collection(FirebaseContracts.RECIPE_COLLECTION).document()
        val userRef = firestore.collection(FirebaseContracts.USER_COLLECTION).document(auth.uid!!)
        val recipeLikesRef = firestore.collection(FirebaseContracts.LIKES_COLLECTION).document()

        val recipeImageRef = "/recipes/${recipeDocumentRef.id}.jpg"

        val imageUrl = imageUploader.uploadImage(
            imageUri = recipeImageUri,
            referencePath = recipeImageRef,
            quality = ImageCompressor.MID_QUALITY
        )

        val recipeData = hashMapOf(
            FirebaseContracts.RECIPE_USER_ID to auth.uid,
            FirebaseContracts.RECIPE_NAME to recipeName,
            FirebaseContracts.RECIPE_IMAGE to imageUrl.data,
            FirebaseContracts.RECIPE_INGREDIENTS to ingredients.map {
                hashMapOf(
                    FirebaseContracts.INGREDIENT_ID to it.id,
                    FirebaseContracts.INGREDIENT_NAME to it.name,
                    FirebaseContracts.INGREDIENT_AMOUNT to it.amount,
                    FirebaseContracts.INGREDIENT_AMOUNT_TYPE to it.amountType
                )
            },
            FirebaseContracts.RECIPE_PROCESS to recipeProcess,
            FirebaseContracts.RECIPE_TIMESTAMP to Timestamp.now(),
            FirebaseContracts.RECIPE_LIKES_REF to recipeLikesRef
        )

        val recipeLikeData = hashMapOf(
            FirebaseContracts.LIKE_RECIPE_USER_ID to auth.uid!!,
            FirebaseContracts.LIKE_RECIPE_ID to recipeDocumentRef.id,
            FirebaseContracts.LIKE_RECIPE_NAME to recipeName,
            FirebaseContracts.LIKE_RECIPE_IMAGE to imageUrl.data,
            FirebaseContracts.LIKE_USERS to emptyList<String>()
        )

        return suspendCoroutine { continuation ->
            firestore.runBatch { batch ->

                Log.i("upload_recipe", "recipeId: ${recipeDocumentRef.id}")

                batch.set(recipeDocumentRef, recipeData)
                batch.update(userRef, FirebaseContracts.USER_POSTS, FieldValue.increment(1))
                batch.set(recipeLikesRef, recipeLikeData)

            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(Response.Success(data = true))
                } else {
                    storage.reference.child(recipeImageRef).delete()
                    continuation.resume(Response.Failiure(data = false, throwable = task.exception))

                }
            }
        }
    }

}