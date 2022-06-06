package es.javier.cappcake.domain.user.use_cases

import android.graphics.Bitmap
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.UserRepository
import javax.inject.Inject

class LoadProfileImageUseCase @Inject constructor(private val repository: UserRepository) {

    /**
     * Method to load the profile image
     *
     * @param url The url path of the image
     * @return The response with the bitmap of the image
     */
    suspend operator fun invoke(url: String) : Response<Bitmap?> = repository.loadProfileImage(url)

}