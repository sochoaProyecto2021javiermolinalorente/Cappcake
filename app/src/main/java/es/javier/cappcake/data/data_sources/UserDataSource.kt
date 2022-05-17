package es.javier.cappcake.data.data_sources

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import es.javier.cappcake.domain.Recipe
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.User
import es.javier.cappcake.utils.ImageCompressor
import kotlinx.coroutines.coroutineScope
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserDataSource @Inject constructor(private val compressor: ImageCompressor) {

    private val auth = Firebase.auth
    private val firestore = Firebase.firestore
    private val storage = Firebase.storage

    suspend fun authenticateUser(email: String, password: String) : Response<Boolean> {
        return suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Response.Success(true))
                    } else {
                        continuation.resume(Response.Failiure(task.exception!!.message!!, false))
                    }
                }
        }
    }

    suspend fun registerUser(username: String, email: String, password: String, profileImage: Uri?) : Response<Boolean> {
        val response = suspendCoroutine<Response<Boolean>> { continuation ->

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Response.Success(data = true))
                    } else {
                        continuation.resume(Response.Failiure(message = null, data = false))
                    }
                }
        }

        return when (response) {
            is Response.Failiure -> {
                response
            }
            is Response.Success -> {
                if (adduserToFirestore(username, email, auth.currentUser!!.uid, profileImage))
                    Response.Success(data = true)
                else
                    Response.Failiure(message = null, data = false)
            }
        }
    }

    private suspend fun adduserToFirestore(username: String, email: String, uid: String, profileImage: Uri?) : Boolean {

        val imageUrl = uploadProfileImage(profileImage)

        val data = hashMapOf(
            "email" to email,
            "username" to username,
            "profileImage" to imageUrl.data
        )

        return suspendCoroutine<Boolean> { continuation ->
            firestore.collection("users").document(uid).set(data)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(true)
                    } else {
                        Firebase.auth.currentUser!!.delete()
                        continuation.resume(false)
                    }
                }
        }
    }

    private suspend fun uploadProfileImage(recipeImageUri: Uri?) : Response<Uri?> {

        val recipeImageRef = storage.reference.child("${auth.uid}/profile_image/profile-image.jpg")

        if (recipeImageUri == null) return Response.Failiure(data = null, message = null)

        val recipeImage = compressor.comporessBitmap(ImageCompressor.LOW_QUALITY, recipeImageUri)
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

    suspend fun getUserProfile(uid: String) : Response<User?> {
       return suspendCoroutine {  continuation ->
           firestore.collection("users").document(uid).get(Source.SERVER).addOnCompleteListener { task ->
               if (task.isSuccessful) {
                   if (task.result.exists()) {
                       continuation.resume(Response.Success(data = User(
                           userId = task.result.id,
                           username = task.result.getString("username") ?: "Unknown",
                           email = task.result.getString("email") ?: "Unknown",
                           profileImage = task.result.getString("profileImage"),
                       )))
                   } else {
                       continuation.resume(Response.Failiure(data = null, message = null))
                   }

               } else {
                   continuation.resume(Response.Failiure(data = null, message = null))
               }
           }
       }
    }

}
