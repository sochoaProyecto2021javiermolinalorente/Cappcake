package es.javier.cappcake.domain.activity.usec_ases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.activity.Activity
import es.javier.cappcake.domain.repositories.ActivityRepository
import javax.inject.Inject

class GetActivitiesUseCase @Inject constructor(private val repository: ActivityRepository) {

    suspend operator fun invoke(lastActivityId: String?) : Response<Pair<List<Activity>, String>> = repository.getActivities(lastActivityId)

}