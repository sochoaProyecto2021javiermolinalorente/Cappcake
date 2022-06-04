package es.javier.cappcake.data.data_sources.activity

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.activity.Activity
import javax.inject.Inject

class ActivityDataSource @Inject constructor(
    private val getActivities: GetActivities
) {

    suspend fun getActivities(lastActivityId: String?) : Response<Pair<List<Activity>, String>> {
        return getActivities.getActivity(lastActivityId)
    }

}