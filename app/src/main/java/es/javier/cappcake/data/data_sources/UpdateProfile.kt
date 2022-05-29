package es.javier.cappcake.data.data_sources

import android.net.Uri
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import es.javier.cappcake.data.entities.FirebaseContracts
import es.javier.cappcake.domain.Response
import es.javier.cappcake.utils.ImageCompressor
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UpdateProfile @Inject constructor(
    private val imageUploader: ImageUploader
) {

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth

    suspend fun updateProfile(username: String, profileImage: Uri?) : Response<Boolean> {

        val currentProfileImageUrl: Uri? = suspendCoroutine { continuation ->
            Firebase.storage.reference.child(auth.uid!! + FirebaseContracts.USER_PROFILE_IMAGE_REFERENCE)
                .downloadUrl
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(task.result)
                    } else {
                        continuation.resume(null)
                    }
            }
        }

        // Get or delete imageProfile
        val newProfileImageUrl = if (currentProfileImageUrl != profileImage) {
            imageUploader.uploadImage(
                profileImage,
                FirebaseContracts.USER_PROFILE_IMAGE_REFERENCE,
                ImageCompressor.LOW_QUALITY
            ).data
        } else {
            currentProfileImageUrl
        }

        return suspendCoroutine { continuation ->
            firestore.runTransaction { transaction ->

                // Get user document
                val userRef = firestore.collection(FirebaseContracts.USER_COLLECTION).document(auth.uid!!)
                val userDocument = transaction.get(userRef)

                // Get user followers
                val followersRef = firestore.collection(FirebaseContracts.FOLLOWERS_COLLECTION).document(userDocument.getString(FirebaseContracts.USER_NAME)!!)
                val followersDocument = transaction.get(followersRef)

                // Create username index

                // Delete username index

                if (userDocument.getString(FirebaseContracts.USER_NAME) == username) {
                    // Update followers document
                    transaction.update(followersRef, FirebaseContracts.FOLLOWERS_PROFILE_IMAGE, newProfileImageUrl)
                } else {
                    // Create user followers
                    val followersData = hashMapOf(
                        FirebaseContracts.FOLLOWERS_USER_ID to userDocument.id,
                        FirebaseContracts.FOLLOWERS_PROFILE_IMAGE to newProfileImageUrl,
                        FirebaseContracts.FOLLOWERS_USERS to followersDocument.get(
                            FirebaseContracts.FOLLOWERS_USERS
                        )
                    )

                    val newFollowersRef = firestore.collection(FirebaseContracts.FOLLOWERS_COLLECTION).document(username)
                    transaction.set(newFollowersRef, followersData)

                    // Delete user followers
                    transaction.delete(followersRef)
                }

                // Update user document data
                transaction.update(
                    userRef,
                    mapOf(
                        FirebaseContracts.USER_NAME to username,
                        FirebaseContracts.USER_PROFILE_IMAGE to newProfileImageUrl
                    )
                )

            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(Response.Success(data = true))
                } else {
                    continuation.resume(Response.Failiure(data = false, message = null))
                }

            }
        }

    }

}