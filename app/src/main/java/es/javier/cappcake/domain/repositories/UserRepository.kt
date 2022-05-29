package es.javier.cappcake.domain.repositories

import android.graphics.Bitmap
import android.net.Uri
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.user.User

interface UserRepository {
    fun getCurrentUserId() : String?
    suspend fun authenticateUser(email: String, password: String) : Response<Boolean>
    suspend fun registerUser(username: String, email: String, password: String, image: Uri? = null) : Response<Boolean>
    suspend fun getUserProfile(uid: String) : Response<Pair<User, Boolean>?>
    suspend fun loadProfileImage(url: String) : Response<Bitmap?>
    suspend fun getFollowersCount(uid: String) : Response<Int?>
    suspend fun getFollowedUsers() : Response<List<User>>
    suspend fun followUser(followedUserId: String) : Response<Boolean>
    suspend fun unfollowUser(unfollowedUserId: String) : Response<Boolean>
    suspend fun updateProfile(username: String, profileImageUri: Uri?) : Response<Boolean>
}