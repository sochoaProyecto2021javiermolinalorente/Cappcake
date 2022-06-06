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

    /**
     * Method to update the content of a recipe in firestore
     *
     * @param recipeId The id of the recipe to update
     * @param recipeName The new recipe name of the recipe
     * @param recipeImageUri The new recipe image of the recipe
     * @param recipeProcess The new recipe process of the recipe
     * @param ingredients The new list of ingredients of the recipe
     * @return The response with the status of the operation
     */
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
            FirebaseContracts.RECIPE_INGREDIENTS to ingredients.map {
                hashMapOf(
                    FirebaseContracts.INGREDIENT_ID to it.id,
                    FirebaseContracts.INGREDIENT_NAME to it.name,
                    FirebaseContracts.INGREDIENT_AMOUNT to it.amount,
                    FirebaseContracts.INGREDIENT_AMOUNT_TYPE to it.amountType
                )
            },
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