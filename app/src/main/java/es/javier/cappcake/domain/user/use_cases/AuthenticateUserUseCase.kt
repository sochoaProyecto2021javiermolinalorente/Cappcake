package es.javier.cappcake.domain.user.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.UserRepository
import javax.inject.Inject

class AuthenticateUserUseCase @Inject constructor(private val repository: UserRepository) {

    suspend operator fun invoke(email: String, password: String) : Response<Boolean> = repository.authenticateUser(email, password)

}