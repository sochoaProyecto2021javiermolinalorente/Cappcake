package es.javier.cappcake.data.data_sources.user

import android.net.Uri
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import es.javier.cappcake.data.data_sources.ImageUploader
import es.javier.cappcake.data.entities.FirebaseContracts
import es.javier.cappcake.domain.Response
import es.javier.cappcake.utils.ImageCompressor
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RegisterUser @Inject constructor(private val imageUploader: ImageUploader) {

    private val auth = Firebase.auth
    private val firestore = Firebase.firestore
    private val storage = Firebase.storage


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

                    // Add followers data
                    val followersRef = firestore.collection(FirebaseContracts.FOLLOWERS_COLLECTION).document(username)
                    batch.set(followersRef, followersData)

                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Response.Success(data = true))
                    } else {
                        val ex = task.exception
                        val profileImageRef = storage.reference.child(auth.uid!! + FirebaseContracts.USER_PROFILE_IMAGE_REFERENCE)
                        profileImageRef.delete()
                        auth.currentUser?.delete()
                        continuation.resume(Response.Failiure(data = false, throwable = null))
                    }
                }
            }

        } else {
            return response
        }

    }


}