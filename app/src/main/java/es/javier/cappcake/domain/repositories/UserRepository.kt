package es.javier.cappcake.domain.repositories

import android.graphics.Bitmap
import android.net.Uri
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.user.User

interface UserRepository {

    /**
     * Method to get the current user id
     *
     * @return The current user id
     */
    fun getCurrentUserId() : String?

    /**
     * Method to authenticate user
     *
     * @param email The email of the user
     * @param password The password of the user
     * @return The response with the status of the operation
     */
    suspend fun authenticateUser(email: String, password: String) : Response<Boolean>

    /**
     * Method to sign out
     *
     * @return The response with the status of the operation
     */
    suspend fun signOut() : Response<Boolean>

    /**
     * Method to register a user
     *
     * @param username The username of the new user
     * @param email The email of the new user
     * @param password the password of the new user
     * @param image The uri of the image of the new user
     * @return The response with the status of the operation
     */
    suspend fun registerUser(username: String, email: String, password: String, image: Uri? = null) : Response<Boolean>

    /**
     * Method to get a user
     *
     * @param uid The id of the user
     * @return The response with user and a boolean that represents if the current user follows that user
     */
    suspend fun getUserProfile(uid: String) : Response<Pair<User, Boolean>?>

    /**
     * Method to load the profile image
     *
     * @param url The url path of the image
     * @return The response with the bitmap of the image
     */
    suspend fun loadProfileImage(url: String) : Response<Bitmap?>

    /**
     * Method to get a list of users whose has the username passed
     *
     * @param username The username of the users to search
     * @return The response with the list of users that matches that username
     */
    suspend fun getUsersWhereName(username: String) : Response<List<User>>

    /**
     * Method to get how many followers has a user
     *
     * @param uid The id of the user
     * @return The response with the number of followers
     */
    suspend fun getFollowersCount(uid: String) : Response<Int?>

    /**
     * Method to get the user that current user follows
     *
     * @return The response with the list of users
     */
    suspend fun getFollowedUsers() : Response<List<User>>

    /**
     * Method to follow an user
     *
     * @param followedUserId The if of the user to follow
     * @return The response with the status of the operation
     */
    suspend fun followUser(followedUserId: String) : Response<Boolean>

    /**
     * Method to unfollow a user
     *
     * @param unfollowedUserId The id of the user to unfollow
     * @return The response with the status of the operation
     */
    suspend fun unfollowUser(unfollowedUserId: String) : Response<Boolean>

    /**
     * Method to update the profile of the current user
     *
     * @param username The new username of the user
     * @param profileImageUri The new image of the user
     * @return The response with status of the operation
     */
    suspend fun updateProfile(username: String, profileImageUri: Uri?) : Response<Boolean>
}