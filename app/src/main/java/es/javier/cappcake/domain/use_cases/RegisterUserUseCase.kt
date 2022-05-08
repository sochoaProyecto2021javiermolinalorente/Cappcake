package es.javier.cappcake.domain.use_cases

import android.graphics.Bitmap
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.UserRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(private val repository: UserRepository) {

    suspend operator fun invoke(username: String, email: String, password: String, image: Bitmap?) : Response<Boolean> =
        repository.registerUser(username, email, password, image)

}