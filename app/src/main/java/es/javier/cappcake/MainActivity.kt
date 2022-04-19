package es.javier.cappcake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import es.javier.cappcake.presentation.Navigation
import es.javier.cappcake.presentation.activityscreen.ActivityScreen
import es.javier.cappcake.presentation.addrecipescreen.AddRecipeScreen
import es.javier.cappcake.presentation.feedscreen.FeedScreen
import es.javier.cappcake.presentation.loginscreen.LoginScreen
import es.javier.cappcake.presentation.loginscreen.LoginScreenViewModel
import es.javier.cappcake.presentation.profilescreen.ProfileScreen
import es.javier.cappcake.presentation.registerscreen.RegisterScreen
import es.javier.cappcake.presentation.registerscreen.RegisterScreenViewModel
import es.javier.cappcake.presentation.searchscreen.SearchScreen
import es.javier.cappcake.presentation.ui.theme.CappcakeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CappcakeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavigationGraph()
                }
            }
        }
    }
}

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Navigation.AUTHENTCATION_GRAPH) {
        LoginGraph(navController = navController)
        ApplicationGraph(navController = navController)
    }
    
}

fun NavGraphBuilder.LoginGraph(navController: NavController) {
    navigation(startDestination = Navigation.RegisterScreen.navigationRoute, route = Navigation.AUTHENTCATION_GRAPH) {
        composable(route = Navigation.LoginScreen.navigationRoute) {
            val viewModel = viewModel<LoginScreenViewModel>()
            LoginScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = Navigation.RegisterScreen.navigationRoute) {
            val viewModel = viewModel<RegisterScreenViewModel>()
            RegisterScreen(navController = navController, viewModel = viewModel)
        }
    }
}

fun NavGraphBuilder.ApplicationGraph(navController: NavController) {
    navigation(startDestination = Navigation.FeedScreen.navigationRoute, route = Navigation.APPLICATION_GRAPH) {
        composable(Navigation.FeedScreen.navigationRoute) {
            FeedScreen(navController = navController)
        }
        composable(Navigation.SearchScreen.navigationRoute) {
            SearchScreen(navController = navController)
        }
        composable(Navigation.AddRecipeScreen.navigationRoute) {
            AddRecipeScreen(navController = navController)
        }
        composable(Navigation.ActivityScreen.navigationRoute) {
            ActivityScreen(navController = navController)
        }
        composable(Navigation.ProfileScreen.navigationRoute) {
            ProfileScreen(navController = navController)
        }
    }
}
