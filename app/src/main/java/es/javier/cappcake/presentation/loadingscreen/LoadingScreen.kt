package es.javier.cappcake.presentation.loadingscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import es.javier.cappcake.Navigation

@Composable
fun LoadingScreen(navController: NavController, viewModel: LoadingScreenViewModel) {

    val userId = viewModel.getCurrentUserId()

    LaunchedEffect(key1 = Unit) {
        if (userId == null) {
            navController.navigate(Navigation.AUTHENTCATION_GRAPH) {
                popUpTo(Navigation.LoadingScreen.navigationRoute) {
                    inclusive = true
                }
            }
        } else {
            navController.navigate(Navigation.APPLICATION_GRAPH) {
                popUpTo(Navigation.LoadingScreen.navigationRoute) {
                    inclusive = true
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }

}