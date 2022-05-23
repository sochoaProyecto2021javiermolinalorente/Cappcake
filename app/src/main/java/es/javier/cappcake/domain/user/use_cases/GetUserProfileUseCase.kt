package es.javier.cappcake.domain.user.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.user.User
import es.javier.cappcake.domain.repositories.UserRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(private val repository: UserRepository) {

    suspend operator fun invoke(uid: String) : Response<Pair<User, Boolean>?> = repository.getUserProfile(uid = uid)

}