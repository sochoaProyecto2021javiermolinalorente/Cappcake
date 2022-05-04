package es.javier.cappcake.domain.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.UserRepository

class AuthenticateUserUseCase(private val repository: UserRepository) {

    suspend operator fun invoke(email: String, password: String) : Response<Boolean> = repository.authenticateUser(email, password)

}