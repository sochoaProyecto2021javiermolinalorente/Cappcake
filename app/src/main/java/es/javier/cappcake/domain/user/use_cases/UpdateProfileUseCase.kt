package es.javier.cappcake.domain.user.use_cases

import android.net.Uri
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.UserRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(private val repository: UserRepository) {

    /**
     * Method to update the profile of the current user
     *
     * @param username The new username of the user
     * @param profileImageUri The new image of the user
     * @return The response with status of the operation
     */
    suspend operator fun invoke(username: String, profileImageUri: Uri?) : Response<Boolean> = repository.updateProfile(username, profileImageUri)

}