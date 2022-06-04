package es.javier.cappcake.data.repositories

import es.javier.cappcake.data.data_sources.activity.ActivityDataSource
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.activity.Activity
import es.javier.cappcake.domain.repositories.ActivityRepository
import javax.inject.Inject

class ImplActivityRepository @Inject constructor(private val dataSource: ActivityDataSource) : ActivityRepository {

    override suspend fun getActivities(lastActivityId: String?): Response<Pair<List<Activity>, String>> {
        return dataSource.getActivities(lastActivityId)
    }

}