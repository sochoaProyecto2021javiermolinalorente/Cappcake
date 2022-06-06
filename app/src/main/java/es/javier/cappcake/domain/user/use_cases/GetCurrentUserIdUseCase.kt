package es.javier.cappcake.domain.user.use_cases

import es.javier.cappcake.domain.repositories.UserRepository
import javax.inject.Inject

class GetCurrentUserIdUseCase @Inject constructor(private val repository: UserRepository) {

    /**
     * Method to get the current user id
     *
     * @return The current user id
     */
    operator fun invoke() : String? = repository.getCurrentUserId()

}