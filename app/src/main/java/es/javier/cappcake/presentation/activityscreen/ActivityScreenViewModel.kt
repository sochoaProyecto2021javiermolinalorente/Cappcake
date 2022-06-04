package es.javier.cappcake.presentation.activityscreen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.activity.Activity
import es.javier.cappcake.domain.activity.usec_ases.GetActivitiesUseCase
import es.javier.cappcake.domain.user.User
import es.javier.cappcake.domain.user.use_cases.GetUserProfileUseCase
import es.javier.cappcake.utils.ScreenState
import javax.inject.Inject

@HiltViewModel
class ActivityScreenViewModel @Inject constructor(
    private val getActivitiesUseCase: GetActivitiesUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {

    val activities = mutableStateListOf<Activity>()
    var lastActivityId: String? by mutableStateOf(null)

    var isRefreshing by mutableStateOf(false)
    var screenState: ScreenState by mutableStateOf(ScreenState.LoadingData)

    suspend fun loadActivities() {
        val response = getActivitiesUseCase(null)

        when (response) {
            is Response.Failiure -> { }
            is Response.Success -> {
                activities.clear()
                activities.addAll(response.data!!.first)
                lastActivityId = response.data.second
                screenState = ScreenState.DataLoaded
            }
        }
    }

    suspend fun loadActivitiesAgain() {
        isRefreshing = true
        val response = getActivitiesUseCase(null)

        when (response) {
            is Response.Failiure -> { isRefreshing = false }
            is Response.Success -> {
                activities.clear()
                activities.addAll(response.data!!.first)
                lastActivityId = response.data.second
                isRefreshing = false
            }
        }
    }

    suspend fun loadMoreActivities() {
        val response = getActivitiesUseCase(lastActivityId)

        when (response) {
            is Response.Failiure -> { }
            is Response.Success -> {
                activities.addAll(response.data!!.first)
                lastActivityId = response.data.second
            }
        }
    }

    suspend fun loadUser(uid: String) : User? {
        val response = getUserProfileUseCase(uid = uid)

        return when (response) {
            is Response.Failiure -> null
            is Response.Success -> response.data?.first
        }
    }

}