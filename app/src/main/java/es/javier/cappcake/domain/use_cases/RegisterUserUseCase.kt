package es.javier.cappcake.domain.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.User
import es.javier.cappcake.domain.repositories.UserRepository

class RegisterUserUseCase(val repository: UserRepository) {

    suspend operator fun invoke(user: User) : Response<Boolean> = repository.registerUser(user = user)

}