package es.javier.cappcake.data.data_sources.activity

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.activity.Activity
import javax.inject.Inject

class ActivityDataSource @Inject constructor(
    private val getActivities: GetActivities
) {

    /**
     * Method to get an amount of activities of a user from the activity of the user Id
     *
     * @param lastActivityId the last activity id collected by a previous call
     * @return The response with the list of activities and the id of the last activity in the list
     */
    suspend fun getActivities(lastActivityId: String?) : Response<Pair<List<Activity>, String>> {
        return getActivities.getActivity(lastActivityId)
    }

}