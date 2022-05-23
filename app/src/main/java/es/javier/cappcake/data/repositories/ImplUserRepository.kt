package es.javier.cappcake.data.repositories

import android.net.Uri
import es.javier.cappcake.data.data_sources.UserDataSource
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.user.User
import es.javier.cappcake.domain.repositories.UserRepository
import javax.inject.Inject

class ImplUserRepository @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository {

    override fun getCurrentUserId(): String? = userDataSource.getCurrentUserId()

    override suspend fun authenticateUser(email: String, password: String) : Response<Boolean> =
        userDataSource.authenticateUser(email, password)

    override suspend fun registerUser(username: String, email: String, password: String, image: Uri?) : Response<Boolean> =
        userDataSource.registerUser(username, email, password, image)

    override suspend fun getUserProfile(uid: String): Response<Pair<User, Boolean>?> = userDataSource.getUserProfile(uid = uid)
    override suspend fun getFollowersCount(uid: String): Response<Int?> = userDataSource.getFollowersCount(uid = uid)

    override suspend fun followUser(followedUserId: String): Response<Boolean> = userDataSource.followUser(followedUserId)

    override suspend fun unfollowUser(unfollowedUserId: String): Response<Boolean> = userDataSource.unfollowUser(unfollowedUserId)
}