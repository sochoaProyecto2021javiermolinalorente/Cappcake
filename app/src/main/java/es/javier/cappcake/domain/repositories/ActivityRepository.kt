package es.javier.cappcake.domain.repositories

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.activity.Activity

interface ActivityRepository {

    /**
     * Method to get an amount of activities of a user
     *
     * @param lastActivityId The last activity id of a previous call
     * @return The response with the List of activties and the id of the las activity in the list
     */
    suspend fun getActivities(lastActivityId: String?) : Response<Pair<List<Activity>, String>>

}