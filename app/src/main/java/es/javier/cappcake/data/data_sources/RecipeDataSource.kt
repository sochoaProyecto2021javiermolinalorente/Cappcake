package es.javier.cappcake.data.data_sources

import android.graphics.Bitmap
import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import es.javier.cappcake.domain.Ingredient
import es.javier.cappcake.domain.Response
import es.javier.cappcake.utils.ImageCompressor
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RecipeDataSource @Inject constructor(private val imageCompressor: ImageCompressor) {

    suspend fun uploadRecipe(recipeName: String, recipeImageUri: Uri?, recipeProcess: String, ingredients: List<Ingredient>) : Response<Boolean> {

        val firestore = Firebase.firestore
        val auth = Firebase.auth

        val recipeDocumentRef = firestore.collection("recipes").document()

        val imageUrl = uploadRecipeImage(recipeImageUri)

        val data = hashMapOf(
            "user" to auth.uid,
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
        val auth = Firebase.auth
        val storage = Firebase.storage

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

}