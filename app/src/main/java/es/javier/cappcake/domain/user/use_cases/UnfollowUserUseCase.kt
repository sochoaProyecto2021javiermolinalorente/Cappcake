package es.javier.cappcake.domain.user.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.UserRepository
import javax.inject.Inject

class UnfollowUserUseCase @Inject constructor(private val repository: UserRepository) {

    /**
     * Method to unfollow a user
     *
     * @param unfollowedUserId The id of the user to unfollow
     * @return The response with the status of the operation
     */
    suspend operator fun invoke(unfollowedUserId: String) : Response<Boolean> = repository.unfollowUser(unfollowedUserId = unfollowedUserId)

}