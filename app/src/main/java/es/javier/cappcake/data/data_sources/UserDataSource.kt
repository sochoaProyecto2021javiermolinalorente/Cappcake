package es.javier.cappcake.data.data_sources

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import es.javier.cappcake.data.entities.FirebaseContracts
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.user.User
import es.javier.cappcake.utils.ImageCompressor
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserDataSource @Inject constructor(
    private val registerUserDataSource: RegisterUserDataSource,
    private val getProfile: GetProfile
) {

    private val auth = Firebase.auth

    fun getCurrentUserId() : String? = auth.uid

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
        return registerUserDataSource.registerUser(username, email, password, profileImage)
    }

    suspend fun getUserProfile(uid: String) : Response<User?> {
        return getProfile.getUserProfile(uid = uid)
    }

    /*suspend fun getUserProfile(uid: String) : Response<User?> {

       return suspendCoroutine {  continuation ->
           firestore.collection(FirebaseContracts.USER_COLLECTION).document(uid).get(Source.SERVER).addOnCompleteListener { task ->
               if (task.isSuccessful) {
                   if (task.result.exists()) {
                       continuation.resume(Response.Success(data = User(
                           userId = task.result.id,
                           username = task.result.getString(FirebaseContracts.USER_NAME) ?: FirebaseContracts.UNKNOWN,
                           email = task.result.getString(FirebaseContracts.USER_EMAIL) ?: FirebaseContracts.UNKNOWN,
                           profileImage = task.result.getString(FirebaseContracts.USER_PROFILE_IMAGE),
                           posts = task.result.getLong(FirebaseContracts.USER_POSTS)?.toInt() ?: FirebaseContracts.NUMBER_UNKNOWN,
                           followers = task.result.getLong(FirebaseContracts.USER_FOLLOWERS)?.toInt() ?: FirebaseContracts.NUMBER_UNKNOWN,
                           following = task.result.getLong(FirebaseContracts.USER_FOLLOWING)?.toInt() ?: FirebaseContracts.NUMBER_UNKNOWN)
                       ))
                   } else {
                       continuation.resume(Response.Failiure(data = null, message = null))
                   }

               } else {
                   continuation.resume(Response.Failiure(data = null, message = null))
               }
           }
       }
    }*/

}
