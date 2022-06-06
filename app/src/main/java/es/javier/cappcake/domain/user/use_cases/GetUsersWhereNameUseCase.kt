package es.javier.cappcake.domain.user.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.UserRepository
import es.javier.cappcake.domain.user.User
import javax.inject.Inject

class GetUsersWhereNameUseCase @Inject constructor(private val repository: UserRepository) {

    /**
     * Method to get a list of users whose has the username passed
     *
     * @param username The username of the users to search
     * @return The response with the list of users that matches that username
     */
    suspend operator fun invoke(username: String) : Response<List<User>> = repository.getUsersWhereName(username)

}