package es.javier.cappcake.domain.user.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.UserRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val repository: UserRepository) {

    /**
     * Method to sign out
     *
     * @return The response with the status of the operation
     */
    suspend operator fun invoke() : Response<Boolean> = repository.signOut()

}