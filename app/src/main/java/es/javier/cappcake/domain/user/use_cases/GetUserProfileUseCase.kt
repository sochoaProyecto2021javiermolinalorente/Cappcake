package es.javier.cappcake.domain.user.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.user.User
import es.javier.cappcake.domain.repositories.UserRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(private val repository: UserRepository) {

    /**
     * Method to get a user
     *
     * @param uid The id of the user
     * @return The response with user and a boolean that represents if the current user follows that user
     */
    suspend operator fun invoke(uid: String) : Response<Pair<User, Boolean>?> = repository.getUserProfile(uid = uid)

}