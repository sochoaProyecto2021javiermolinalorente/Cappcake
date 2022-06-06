package es.javier.cappcake.data.data_sources.activity

import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.javier.cappcake.data.data_sources.CalendarUtil
import es.javier.cappcake.data.entities.FirebaseContracts
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.activity.Activity
import es.javier.cappcake.domain.activity.ActivityType
import java.lang.IllegalArgumentException
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GetActivities @Inject constructor(private val calendarUtil: CalendarUtil) {

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth

    /**
     * Method to get an amount of activities of a user from the activity of the user Id
     *
     * @param lastActivityId the last activity id collected by a previous call
     * @return The response with the list of activities and the id of the last activity in the list
     */
    suspend fun getActivity(lastActivityId: String?) : Response<Pair<List<Activity>, String>> {

        val activityCollection = firestore.collection(FirebaseContracts.ACTIVITY_COLLECTION)

        val lastDocumentSnapshot: DocumentSnapshot? = if (lastActivityId != null) {
            suspendCoroutine { continuation ->
                activityCollection
                    .document(lastActivityId)
                    .get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(task.result)
                        } else {
                            continuation.resume(null)
                        }
                    }
            }
        } else {
            null
        }

        val query = try {
            if (lastDocumentSnapshot != null) {
                activityCollection
                    .whereEqualTo(FirebaseContracts.ACTIVITY_AFFECTED_USER_ID, auth.uid!!)
                    .whereGreaterThanOrEqualTo(FirebaseContracts.ACTIVITY_TIMESTAMP, Timestamp(calendarUtil.getLastWeekDate()))
                    .orderBy(FirebaseContracts.ACTIVITY_TIMESTAMP, Query.Direction.DESCENDING)
                    .startAfter(lastDocumentSnapshot)
                    .limit(20)
            } else {
                activityCollection
                    .whereEqualTo(FirebaseContracts.ACTIVITY_AFFECTED_USER_ID, auth.uid!!)
                    .whereGreaterThanOrEqualTo(FirebaseContracts.ACTIVITY_TIMESTAMP, Timestamp(calendarUtil.getLastWeekDate()))
                    .orderBy(FirebaseContracts.ACTIVITY_TIMESTAMP, Query.Direction.DESCENDING)
                    .limit(20)

            }
        } catch (ex: IllegalArgumentException) {
            return Response.Failiure(data = null, throwable = ex)
        }

        return suspendCoroutine { continuation ->
            query.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val activities = task.result.map { document ->
                        val activityId = document.id
                        val userId = document.getString(FirebaseContracts.ACTIVITY_USER_ID) ?: ""
                        val affectedUserId = document.getString(FirebaseContracts.ACTIVITY_AFFECTED_USER_ID) ?: ""
                        val recipeId = document.getString(FirebaseContracts.ACTIVITY_RECIPE_ID)
                        val activityType = ActivityType.valueOf(document.getString(FirebaseContracts.ACTIVITY_TYPE)!!)
                        return@map Activity(
                            activityId = activityId,
                            userId = userId,
                            affectedUserId = affectedUserId,
                            recipeId = recipeId,
                            activityType = activityType
                        )
                    }

                    if (activities.isEmpty()) {
                        continuation.resume(Response.Failiure(data = null, throwable = null))
                    } else {
                        continuation.resume(Response.Success(data = Pair(activities, activities.last().activityId)))
                    }
                } else {
                    continuation.resume(Response.Failiure(data = null, throwable = task.exception))
                }
            }
        }

    }

}