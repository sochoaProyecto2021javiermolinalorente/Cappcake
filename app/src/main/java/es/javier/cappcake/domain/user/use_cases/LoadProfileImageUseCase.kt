package es.javier.cappcake.domain.user.use_cases

import android.graphics.Bitmap
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.UserRepository
import javax.inject.Inject

class LoadProfileImageUseCase @Inject constructor(private val repository: UserRepository) {

    suspend operator fun invoke(url: String) : Response<Bitmap?> = repository.loadProfileImage(url)

}