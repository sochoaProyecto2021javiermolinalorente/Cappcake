package es.javier.cappcake.domain.repositories

import android.net.Uri
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.user.User

interface UserRepository {
    fun getCurrentUserId() : String?
    suspend fun authenticateUser(email: String, password: String) : Response<Boolean>
    suspend fun registerUser(username: String, email: String, password: String, image: Uri? = null) : Response<Boolean>
    suspend fun getUserProfile(uid: String) : Response<User?>
}