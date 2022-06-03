package es.javier.cappcake.data.data_sources.recipe

import android.net.Uri
import com.google.firebase.auth.ktx.auth
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

class UpdateRecipe @Inject constructor(private val imageUploader: ImageUploader) {

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth

    suspend fun updateRecipe(recipeId: String,
                             recipeName: String,
                             recipeImageUri: Uri?,
                             recipeProcess: String,
                             ingredients: List<Ingredient>) : Response<Boolean> {

        val currentRecipeImage: Uri? = suspendCoroutine { continuation ->
            Firebase.storage.reference.child(auth.uid!! + "/recipes/" + recipeId + ".jpg")
                .downloadUrl
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(task.result)
                    } else {
                        continuation.resume(null)
                    }
                }
        }

        val newRecipeImage = if (currentRecipeImage?.toString() != recipeImageUri.toString()) {
            imageUploader.uploadImage(
                recipeImageUri,
                "/recipes/$recipeId.jpg",
                ImageCompressor.MID_QUALITY
            ).data
        } else {
            currentRecipeImage
        }

        val newRecipeData = hashMapOf(
            FirebaseContracts.RECIPE_NAME to recipeName,
            FirebaseContracts.RECIPE_IMAGE to newRecipeImage,
            FirebaseContracts.RECIPE_INGREDIENTS to ingredients,
            FirebaseContracts.RECIPE_PROCESS to recipeProcess
        )

        val newLikeRecipeData = hashMapOf<String, Any?>(
            FirebaseContracts.LIKE_RECIPE_NAME to recipeName,
            FirebaseContracts.LIKE_RECIPE_IMAGE to newRecipeImage
        )

        return suspendCoroutine { continuation ->
            firestore.runTransaction { transaction ->

                val recipeRef = firestore.collection(FirebaseContracts.RECIPE_COLLECTION).document(recipeId)
                val recipe = transaction.get(recipeRef)
                val recipeLikeRef = recipe.getDocumentReference(FirebaseContracts.RECIPE_LIKES_REF)!!
                val recipeLike = transaction.get(recipeLikeRef)

                // Update recipe
                transaction.update(recipeRef, newRecipeData)

                // Update like recipe
                transaction.update(recipeLikeRef, newLikeRecipeData)

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