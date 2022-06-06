package es.javier.cappcake.domain.activity.usec_ases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.activity.Activity
import es.javier.cappcake.domain.repositories.ActivityRepository
import javax.inject.Inject

class GetActivitiesUseCase @Inject constructor(private val repository: ActivityRepository) {

    /**
     * Method to get an amount of activities of a user
     *
     * @param lastActivityId The last activity id of a previous call
     * @return The response with the List of activties and the id of the las activity in the list
     */
    suspend operator fun invoke(lastActivityId: String?) : Response<Pair<List<Activity>, String>> = repository.getActivities(lastActivityId)

}