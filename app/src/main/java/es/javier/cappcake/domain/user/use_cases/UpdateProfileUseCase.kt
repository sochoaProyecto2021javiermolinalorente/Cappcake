package es.javier.cappcake.domain.user.use_cases

import android.net.Uri
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.UserRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(private val repository: UserRepository) {

    suspend operator fun invoke(username: String, profileImageUri: Uri?) : Response<Boolean> = repository.updateProfile(username, profileImageUri)

}