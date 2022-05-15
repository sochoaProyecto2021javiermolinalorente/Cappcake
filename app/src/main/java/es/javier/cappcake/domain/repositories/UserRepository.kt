package es.javier.cappcake.domain.repositories

import android.graphics.Bitmap
import android.net.Uri
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.User

interface UserRepository {
    suspend fun authenticateUser(email: String, password: String) : Response<Boolean>
    suspend fun registerUser(username: String, email: String, password: String, image: Uri? = null) : Response<Boolean>
}