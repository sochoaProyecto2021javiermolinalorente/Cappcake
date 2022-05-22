package es.javier.cappcake.data.data_sources

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import es.javier.cappcake.domain.Response
import es.javier.cappcake.utils.ImageCompressor
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ImageUploader @Inject constructor(private val compressor: ImageCompressor) {

    private val storage = Firebase.storage
    private val auth = Firebase.auth

    suspend fun uploadImage(imageUri: Uri?, referencePath: String, quality: Int) : Response<Uri?> {

        val recipeImageRef = storage.reference.child("${auth.uid}" + referencePath)

        if (imageUri == null) return Response.Failiure(data = null, message = null)

        val recipeImage = compressor.comporessBitmap(quality = quality, imageUri)
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