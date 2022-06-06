package es.javier.cappcake.domain.user.use_cases

import android.graphics.Bitmap
import android.net.Uri
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.UserRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(private val repository: UserRepository) {

    /**
     * Method to register a user
     *
     * @param username The username of the new user
     * @param email The email of the new user
     * @param password the password of the new user
     * @param image The uri of the image of the new user
     * @return The response with the status of the operation
     */
    suspend operator fun invoke(username: String, email: String, password: String, image: Uri?) : Response<Boolean> =
        repository.registerUser(username, email, password, image)

}