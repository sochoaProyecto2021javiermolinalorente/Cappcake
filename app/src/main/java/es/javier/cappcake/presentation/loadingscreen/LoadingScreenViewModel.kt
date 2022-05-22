package es.javier.cappcake.presentation.loadingscreen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import es.javier.cappcake.domain.user.use_cases.GetCurrentUserIdUseCase
import javax.inject.Inject

@HiltViewModel
class LoadingScreenViewModel @Inject constructor(
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : ViewModel() {

    fun getCurrentUserId() : String? = getCurrentUserIdUseCase()

}