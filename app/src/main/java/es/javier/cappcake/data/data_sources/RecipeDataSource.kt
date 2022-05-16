package es.javier.cappcake.data.data_sources

import android.graphics.Bitmap
import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import es.javier.cappcake.domain.AmountType
import es.javier.cappcake.domain.Ingredient
import es.javier.cappcake.domain.Recipe
import es.javier.cappcake.domain.Response
import es.javier.cappcake.utils.ImageCompressor
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RecipeDataSource @Inject constructor(private val imageCompressor: ImageCompressor) {

    private val auth = Firebase.auth
    private val firestore = Firebase.firestore
    private val storage = Firebase.storage

    suspend fun uploadRecipe(recipeName: String, recipeImageUri: Uri?, recipeProcess: String, ingredients: List<Ingredient>) : Response<Boolean> {

        val recipeDocumentRef = firestore.collection("recipes").document()

        val imageUrl = uploadRecipeImage(recipeImageUri)

        val data = hashMapOf(
            "userId" to auth.uid,
            "recipeName" to recipeName,
            "imagePath" to imageUrl.data,
            "ingredients" to ingredients,
            "recipeProcess" to recipeProcess
        )

        return suspendCoroutine { continuation ->
            recipeDocumentRef.set(data).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(Response.Success(data = true))
                } else {
                    val exception = task.exception
                    if (exception != null)
                        continuation.resume(Response.Failiure(data = false, message = exception.message))
                    else
                        continuation.resume(Response.Failiure(data = false, message = null))
                }
            }
        }

    }

    private suspend fun uploadRecipeImage(recipeImageUri: Uri?) : Response<Uri?> {

        val recipeImageRef = storage.reference.child("${auth.uid}/recipes/${UUID.randomUUID()}.jpg")

        if (recipeImageUri == null) return Response.Failiure(data = null, message = null)

        val recipeImage = imageCompressor.comporessBitmap(ImageCompressor.MID_QUALITY, recipeImageUri)
        val outputStream = ByteArrayOutputStream()
        recipeImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val imageByteArray = outputStream.toByteArray()

        val uploadTask = recipeImageRef.putBytes(imageByteArray)

        return suspendCoroutine { continuation ->

            uploadTask.continueWithTask { task ->
                if (task.isSuccessful) {
                    recipeImageRef.downloadUrl
                } else {
                    task.exception?.let {
                        throw it
                    }
                }
            }.addOnSuccessListener { urlTask ->
                if (urlTask.path != null) {
                    continuation.resume(Response.Success(data = urlTask))
                } else {
                    continuation.resume(Response.Failiure(data = null, message = null))
                }
            }.addOnFailureListener {
                continuation.resume(Response.Failiure(data = null, message = null))
            }

        }
    }

    suspend fun getRecipesOf(uid: String) : Response<List<Recipe>?> {
        val recipesRef = firestore.collection("recipes")

        val query = recipesRef.whereEqualTo("userId", auth.uid)

        return suspendCoroutine { continuation ->
            query.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val recipeList = task.result.documents.map { document ->
                        val recipeId = document.id
                        val userId = document.getString("userId")
                        val recipeName = document.getString("recipeName")
                        val imagePath = document.getString("imagePath")
                        val recipeProcess = document.getString("recipeProcess")
                        val ingrediets = (document["ingredients"] as ArrayList<HashMap<String, Any>>).map {
                            val ingredientId = it["id"] as String
                            val amount = it["amount"] as Double
                            val amountType = it["amountType"] as String
                            val name = it["name"] as String
                            Ingredient(id = ingredientId, name = name, amount = amount.toFloat(), amountType = AmountType.valueOf(amountType))
                        }
                        Recipe(recipeId = recipeId, userId = userId!!, image = imagePath, ingredients = ingrediets, title = recipeName!!, recipeProcess = recipeProcess!!)
                    }
                    continuation.resume(Response.Success(data = recipeList))
                } else {
                    continuation.resume(Response.Failiure(data = null, message = null))
                }
            }
        }



    }

}