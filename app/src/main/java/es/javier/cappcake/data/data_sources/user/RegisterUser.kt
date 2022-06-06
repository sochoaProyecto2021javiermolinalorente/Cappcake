package es.javier.cappcake.data.data_sources.user

import android.net.Uri
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import es.javier.cappcake.data.data_sources.ImageUploader
import es.javier.cappcake.data.entities.FirebaseContracts
import es.javier.cappcake.domain.PermissionException
import es.javier.cappcake.domain.Response
import es.javier.cappcake.utils.ImageCompressor
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RegisterUser @Inject constructor(private val imageUploader: ImageUploader) {

    private val auth = Firebase.auth
    private val firestore = Firebase.firestore
    private val storage = Firebase.storage

    /**
     * Method to register a user
     *
     * @param username The username of the new user
     * @param email The email of the new user
     * @param password the password of the new user
     * @param image The uri of the image of the new user
     * @return The response with the status of the operation
     */
    suspend fun registerUser(username: String, email: String, password: String, profileImage: Uri?) : Response<Boolean> {

        val response = suspendCoroutine<Response<Boolean>> { continuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Response.Success(data = true))
                    } else {
                        continuation.resume(Response.Failiure(throwable = task.exception, data = false))
                    }
                }
        }

        if (response is Response.Success) {
            val imagePath = imageUploader.uploadImage(imageUri = profileImage, referencePath = FirebaseContracts.USER_PROFILE_IMAGE_REFERENCE, quality = ImageCompressor.LOW_QUALITY)

            val userData = hashMapOf(
                FirebaseContracts.USER_NAME to username,
                FirebaseContracts.USER_EMAIL to email,
                FirebaseContracts.USER_PROFILE_IMAGE to imagePath.data,
                FirebaseContracts.USER_POSTS to FirebaseContracts.NUMBER_UNKNOWN,
                FirebaseContracts.USER_FOLLOWING to FirebaseContracts.NUMBER_UNKNOWN)

            val countersData = hashMapOf(
                FirebaseContracts.USER_FOLLOWERS_COUNTER to 0
            )

            val followersData = hashMapOf(
                FirebaseContracts.FOLLOWERS_USER_ID to auth.uid!!,
                FirebaseContracts.FOLLOWERS_PROFILE_IMAGE to imagePath.data,
                FirebaseContracts.FOLLOWERS_USERS to emptyList<String>()
            )

            val usernameIndexData = hashMapOf(
                FirebaseContracts.INDEX_USERNAME_USER_ID to auth.uid
            )

            return suspendCoroutine { continuation ->
                firestore.runBatch { batch ->

                    // Add user data
                    val userRef = firestore.collection(FirebaseContracts.USER_COLLECTION).document(auth.uid!!)
                    batch.set(userRef, userData)

                    // Add followers counter data
                    val followersCounterRef = userRef.collection(FirebaseContracts.USER_FOLLOWERS_COUNTER_COLLECTION)
                    for (i in 0 until FirebaseContracts.USER_FOLLOWER_COUNTERS) {
                        batch.set(followersCounterRef.document(i.toString()), countersData)
                    }

                    // Add Username index
                    val usernameIndexRef = firestore.collection(FirebaseContracts.INDEX_COLLECTION)
                        .document(FirebaseContracts.INDEX_USER_DOCUMENT)
                        .collection(FirebaseContracts.INDEX_USERNAME_COLLECTION)
                        .document(username)
                    batch.set(usernameIndexRef, usernameIndexData)


                    // Add followers data
                    val followersRef = firestore.collection(FirebaseContracts.FOLLOWERS_COLLECTION).document(username)
                    batch.set(followersRef, followersData)

                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Response.Success(data = true))
                    } else {
                        val exception = if (task.exception is FirebaseFirestoreException) {
                            val exception = task.exception as FirebaseFirestoreException
                            if (exception.code.value() == FirebaseContracts.PERMISSION_DENIED)
                                PermissionException()
                            else
                                task.exception
                        } else {
                            task.exception
                        }
                        val profileImageRef = storage.reference.child(auth.uid!! + FirebaseContracts.USER_PROFILE_IMAGE_REFERENCE)
                        profileImageRef.delete()
                        auth.currentUser?.delete()
                        continuation.resume(Response.Failiure(data = false, throwable = exception))
                    }
                }
            }

        } else {
            return response
        }

    }


}