package es.javier.cappcake.data.data_sources

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.scopes.ViewModelScoped
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.User
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserDataSource @Inject constructor() {

    private val authentication = Firebase.auth

    suspend fun authenticateUser(email: String, password: String) : Response<Boolean> {
        return suspendCoroutine { continuation ->
            authentication.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Response.Success(true))
                    } else {
                        continuation.resume(Response.Failiure(task.exception!!.message!!, false))
                    }
                }
        }
    }

    suspend fun registerUser(username: String, email: String, password: String, image: Uri?) : Response<Boolean> {
        val response = suspendCoroutine<Response<Boolean>> { continuation ->

            authentication.createUserWithEmailAndPassword(email, password)
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
                if (adduserToFirestore(username, email, authentication.currentUser!!.uid))
                    Response.Success(data = true)
                else
                    Response.Failiure(message = null, data = false)
            }
        }
    }

    private suspend fun adduserToFirestore(username: String, email: String, uid: String) : Boolean {
        val db = Firebase.firestore

        return suspendCoroutine<Boolean> { continuation ->
            db.collection("User").document(uid).set(User(userId = uid, username = username, email = email, profileImage = null))
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

}
