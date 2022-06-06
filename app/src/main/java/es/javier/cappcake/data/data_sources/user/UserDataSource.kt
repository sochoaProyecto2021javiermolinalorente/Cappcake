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

    /**
     * Method to get the current user id
     *
     * @return The current user id
     */
    fun getCurrentUserId() : String? = auth.uid

    /**
     * Method to authenticate user
     *
     * @param email The email of the user
     * @param password The password of the user
     * @return The response with the status of the operation
     */
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

    /**
     * Method to sign out
     *
     * @return The response with the status of the operation
     */
    suspend fun signOut() : Response<Boolean> {
        auth.signOut()
        return Response.Success(data = true)
    }

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
        return registerUser.registerUser(username, email, password, profileImage)
    }

    /**
     * Method to get a user
     *
     * @param uid The id of the user
     * @return The response with user and a boolean that represents if the current user follows that user
     */
    suspend fun getUserProfile(uid: String) : Response<Pair<User, Boolean>?> {
        return getProfileInfo.getUserProfile(uid = uid)
    }

    /**
     * Method to get a list of users whose has the username passed
     *
     * @param username The username of the users to search
     * @return The response with the list of users that matches that username
     */
    suspend fun getUsersWhereName(username: String) : Response<List<User>> {
        return getUsersWhereName.getUsersWhereName(username)
    }

    /**
     * Method to load the profile image
     *
     * @param url The url path of the image
     * @return The response with the bitmap of the image
     */
    suspend fun loadProfileImage(url: String) : Response<Bitmap?> {
        return loadImage.loadImage(url)
    }

    /**
     * Method to get the user that current user follows
     *
     * @return The response with the list of users
     */
    suspend fun getFollowedUsers() : Response<List<User>> {
        return getFollowedUsers.getFollowedUsers()
    }

    /**
     * Method to follow an user
     *
     * @param followedUserId The if of the user to follow
     * @return The response with the status of the operation
     */
    suspend fun followUser(followedUserId: String) : Response<Boolean> {
        return followUser.invoke(followedUserId = followedUserId)
    }

    /**
     * Method to unfollow a user
     *
     * @param unfollowedUserId The id of the user to unfollow
     * @return The response with the status of the operation
     */
    suspend fun unfollowUser(unfollowedUserId: String) : Response<Boolean> {
        return unfollowUser.unfollowUser(unfollowedUser = unfollowedUserId)
    }

    /**
     * Method to get how many followers has a user
     *
     * @param uid The id of the user
     * @return The response with the number of followers
     */
    suspend fun getFollowersCount(uid: String) : Response<Int?> {
        return getProfileInfo.getFollowersCount(uid = uid)
    }

    /**
     * Method to update the profile of the current user
     *
     * @param username The new username of the user
     * @param profileImage The new image of the user
     * @return The response with status of the operation
     */
    suspend fun updateUserProfile(username: String, profileImage: Uri?) : Response<Boolean> {
        return updateProfile.updateProfile(username, profileImage)
    }

}
