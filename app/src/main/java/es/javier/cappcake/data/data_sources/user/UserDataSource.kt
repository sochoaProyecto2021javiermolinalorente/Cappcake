package es.javier.cappcake.data.data_sources.user

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import es.javier.cappcake.data.data_sources.*
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.user.User
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserDataSource @Inject constructor(
    private val registerUser: RegisterUser,
    private val getProfileInfo: GetProfileInfo,
    private val followUser: FollowUser,
    private val unfollowUser: UnfollowUser,
    private val getFollowedUsers: GetFollowedUsers,
    private val getUsersWhereName: GetUsersWhereName,
    private val loadImage: LoadImage,
    private val updateProfile: UpdateProfile
) {

    private val auth = Firebase.auth

    fun getCurrentUserId() : String? = auth.uid

    suspend fun authenticateUser(email: String, password: String) : Response<Boolean> {

        try {
            Firebase.firestore.firestoreSettings = firestoreSettings {
                isPersistenceEnabled = false
            }
        } catch (e: Exception) { }

        return suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Response.Success(true))
                    } else {
                        continuation.resume(Response.Failiure(task.exception, false))
                    }
                }
        }
    }

    suspend fun signOut() : Response<Boolean> {
        auth.signOut()
        return Response.Success(data = true)
    }

    suspend fun registerUser(username: String, email: String, password: String, profileImage: Uri?) : Response<Boolean> {
        return registerUser.registerUser(username, email, password, profileImage)
    }

    suspend fun getUserProfile(uid: String) : Response<Pair<User, Boolean>?> {
        return getProfileInfo.getUserProfile(uid = uid)
    }

    suspend fun getUsersWhereName(username: String) : Response<List<User>> {
        return getUsersWhereName.getUsersWhereName(username)
    }

    suspend fun loadProfileImage(url: String) : Response<Bitmap?> {
        return loadImage.loadImage(url)
    }

    suspend fun getFollowedUsers() : Response<List<User>> {
        return getFollowedUsers.getFollowedUsers()
    }

    suspend fun followUser(followedUserId: String) : Response<Boolean> {
        return followUser.invoke(followedUserId = followedUserId)
    }

    suspend fun unfollowUser(unfollowedUserId: String) : Response<Boolean> {
        return unfollowUser.unfollowUser(unfollowedUser = unfollowedUserId)
    }

    suspend fun getFollowersCount(uid: String) : Response<Int?> {
        return getProfileInfo.getFollowersCount(uid = uid)
    }

    suspend fun updateUserProfile(username: String, profileImage: Uri?) : Response<Boolean> {
        return updateProfile.updateProfile(username, profileImage)
    }

}
