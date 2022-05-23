package es.javier.cappcake.domain.user.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.UserRepository
import es.javier.cappcake.domain.user.User
import javax.inject.Inject

class GetFollowedUserUseCase @Inject constructor(private val repository: UserRepository) {

    suspend operator fun invoke() : Response<List<User>> = repository.getFollowedUsers()

}