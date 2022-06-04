package es.javier.cappcake.domain.repositories

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.activity.Activity

interface ActivityRepository {

    suspend fun getActivities(lastActivityId: String?) : Response<Pair<List<Activity>, String>>

}