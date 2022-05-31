package es.javier.cappcake.domain.user.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.UserRepository
import es.javier.cappcake.domain.user.User
import javax.inject.Inject

class GetUsersWhereNameUseCase @Inject constructor(private val repository: UserRepository) {

    suspend operator fun invoke(username: String) : Response<List<User>> = repository.getUsersWhereName(username)

}