package es.javier.cappcake.data.repositories

import android.graphics.Bitmap
import android.net.Uri
import es.javier.cappcake.data.data_sources.user.UserDataSource
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.user.User
import es.javier.cappcake.domain.repositories.UserRepository
import javax.inject.Inject

class ImplUserRepository @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository {

    /**
     * Method to get the current user id
     *
     * @return The current user id
     */
    override fun getCurrentUserId(): String? = userDataSource.getCurrentUserId()

    /**
     * Method to authenticate user
     *
     * @param email The email of the user
     * @param password The password of the user
     * @return The response with the status of the operation
     */
    override suspend fun authenticateUser(email: String, password: String) : Response<Boolean> =
        userDataSource.authenticateUser(email, password)

    /**
     * Method to sign out
     *
     * @return The response with the status of the operation
     */
    override suspend fun signOut(): Response<Boolean> {
        return userDataSource.signOut()
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
    override suspend fun registerUser(username: String, email: String, password: String, image: Uri?) : Response<Boolean> =
        userDataSource.registerUser(username, email, password, image)

    /**
     * Method to get a user
     *
     * @param uid The id of the user
     * @return The response with user and a boolean that represents if the current user follows that user
     */
    override suspend fun getUserProfile(uid: String): Response<Pair<User, Boolean>?> = userDataSource.getUserProfile(uid = uid)

    /**
     * Method to load the profile image
     *
     * @param url The url path of the image
     * @return The response with the bitmap of the image
     */
    override suspend fun loadProfileImage(url: String): Response<Bitmap?> = userDataSource.loadProfileImage(url)

    /**
     * Method to get a list of users whose has the username passed
     *
     * @param username The username of the users to search
     * @return The response with the list of users that matches that username
     */
    override suspend fun getUsersWhereName(username: String): Response<List<User>> {
        return userDataSource.getUsersWhereName(username)
    }

    /**
     * Method to get how many followers has a user
     *
     * @param uid The id of the user
     * @return The response with the number of followers
     */
    override suspend fun getFollowersCount(uid: String): Response<Int?> = userDataSource.getFollowersCount(uid = uid)

    /**
     * Method to get the user that current user follows
     *
     * @return The response with the list of users
     */
    override suspend fun getFollowedUsers(): Response<List<User>> = userDataSource.getFollowedUsers()

    /**
     * Method to follow an user
     *
     * @param followedUserId The if of the user to follow
     * @return The response with the status of the operation
     */
    override suspend fun followUser(followedUserId: String): Response<Boolean> = userDataSource.followUser(followedUserId)

    /**
     * Method to unfollow a user
     *
     * @param unfollowedUserId The id of the user to unfollow
     * @return The response with the status of the operation
     */
    override suspend fun unfollowUser(unfollowedUserId: String): Response<Boolean> = userDataSource.unfollowUser(unfollowedUserId)

    /**
     * Method to update the profile of the current user
     *
     * @param username The new username of the user
     * @param profileImageUri The new image of the user
     * @return The response with status of the operation
     */
    override suspend fun updateProfile(username: String, profileImageUri: Uri?): Response<Boolean> = userDataSource.updateUserProfile(username, profileImageUri)
}