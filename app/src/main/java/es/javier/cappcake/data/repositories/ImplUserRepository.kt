package es.javier.cappcake.data.repositories

import android.graphics.Bitmap
import es.javier.cappcake.data.data_sources.UserDataSource
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.User
import es.javier.cappcake.domain.repositories.UserRepository

class ImplUserRepository(val userDataSource: UserDataSource) : UserRepository {

    override suspend fun authenticateUser(email: String, password: String) : Response<Boolean> =
        userDataSource.authenticateUser(email, password)

    override suspend fun registerUser(username: String, email: String, password: String, image: Bitmap?) : Response<Boolean> =
        userDataSource.registerUser(username, email, password, image)
}