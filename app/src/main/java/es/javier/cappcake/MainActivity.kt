package es.javier.cappcake

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import es.javier.cappcake.data.data_sources.UserDataSource
import es.javier.cappcake.data.repositories.ImplUserRepository
import es.javier.cappcake.domain.use_cases.AuthenticateUserUseCase
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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        const val IP_ADDRESS = "10.0.2.2"
    }

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseAuth.getInstance().useEmulator(IP_ADDRESS, 9099)
        FirebaseFirestore.getInstance().useEmulator(IP_ADDRESS, 8080)

        setContent {

            CappcakeTheme {
                navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        val currentBackStackEntry by navController.currentBackStackEntryAsState()
                        Log.i("Navigation", "Current destination: ${navController.currentDestination?.route}")
                        when (currentBackStackEntry?.destination?.route) {
                            Navigation.FeedScreen.navigationRoute,
                            Navigation.SearchScreen.navigationRoute,
                            Navigation.AddRecipeScreen.navigationRoute,
                            Navigation.ActivityScreen.navigationRoute,
                            Navigation.ProfileScreen.navigationRoute ->
                                BottomNavigationitems(navController = navController, currentBackStackEntry = currentBackStackEntry)
                        }
                    }) {
                    NavHost(navController = navController, startDestination = Navigation.LoadingScreen.navigationRoute) {
                        composable(route = Navigation.LoadingScreen.navigationRoute) { backStackEntry ->

                            LaunchedEffect(key1 = Unit) {
                                val user = FirebaseAuth.getInstance().currentUser
                                //val userLogged = backStackEntry.savedStateHandle.get<Boolean>(Navigation.USER_LOGGED)
                                if (user == null) {
                                    navController.navigate(Navigation.AUTHENTCATION_GRAPH)
                                } else {
                                    navController.navigate(Navigation.APPLICATION_GRAPH)
                                }
                            }

                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                        LoginGraph(navController = navController)
                        ApplicationGraph(navController = navController)
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        val destination = navController.currentDestination?.route
        Log.i("Navigation", destination ?: "destination null")
        destination?.let { currentDestination ->
            when (currentDestination) {
                Navigation.FeedScreen.navigationRoute -> finish()
                Navigation.LoginScreen.navigationRoute -> finish()
            }
        }

        super.onBackPressed()
    }

}

fun NavGraphBuilder.LoginGraph(navController: NavController) {
    navigation(startDestination = Navigation.LoginScreen.navigationRoute, route = Navigation.AUTHENTCATION_GRAPH) {
        composable(route = Navigation.LoginScreen.navigationRoute) {
            LoginScreen(navController = navController, viewModel = hiltViewModel())
        }
        composable(route = Navigation.RegisterScreen.navigationRoute) {
            RegisterScreen(navController = navController, viewModel = hiltViewModel())
        }
    }
}

fun NavGraphBuilder.ApplicationGraph(navController: NavController) {
    navigation(startDestination = Navigation.FeedScreen.navigationRoute, route = Navigation.APPLICATION_GRAPH) {
        composable(Navigation.FeedScreen.navigationRoute) {
            FeedScreen(navController = navController, viewModel = viewModel())
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

@Composable
fun BottomNavigationitems(navController: NavController, currentBackStackEntry: NavBackStackEntry?) {
    val currentDestination = currentBackStackEntry?.destination
    BottomNavigation(elevation = 4.dp) {
        BottomNavigationItem(
            selected = currentDestination?.hierarchy?.any { it.route == Navigation.FeedScreen.navigationRoute } == true,
            onClick = { navController.navigate(Navigation.FeedScreen.navigationRoute) {
                popUpTo(Navigation.FeedScreen.navigationRoute) {
                    inclusive = false
                    this.saveState = true
                }
                launchSingleTop = true
                restoreState = true
            } },
            icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = null)})

        BottomNavigationItem(
            selected = currentDestination?.hierarchy?.any { it.route == Navigation.SearchScreen.navigationRoute } == true,
            onClick = { navController.navigate(Navigation.SearchScreen.navigationRoute) {
                popUpTo(Navigation.FeedScreen.navigationRoute) {
                    inclusive = false
                    this.saveState = true
                }
                launchSingleTop = true
                restoreState = true
            } },
            icon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null)})

        BottomNavigationItem(
            selected = currentDestination?.hierarchy?.any { it.route == Navigation.AddRecipeScreen.navigationRoute } == true,
            onClick = { navController.navigate(Navigation.AddRecipeScreen.navigationRoute) {
                popUpTo(Navigation.FeedScreen.navigationRoute) {
                    inclusive = false
                    this.saveState = true
                }
                launchSingleTop = true
                restoreState = true
            } },
            icon = { Icon(imageVector = Icons.Filled.Add, contentDescription = null)})

        BottomNavigationItem(
            selected = currentDestination?.hierarchy?.any { it.route == Navigation.ActivityScreen.navigationRoute } == true,
            onClick = { navController.navigate(Navigation.ActivityScreen.navigationRoute) {
                popUpTo(Navigation.FeedScreen.navigationRoute) {
                    inclusive = false
                    this.saveState = true
                }
                launchSingleTop = true
                restoreState = true
            } },
            icon = { Icon(imageVector = Icons.Filled.Favorite, contentDescription = null)})

        BottomNavigationItem(
            selected = currentDestination?.hierarchy?.any { it.route == Navigation.ProfileScreen.navigationRoute } == true,
            onClick = { navController.navigate(Navigation.ProfileScreen.navigationRoute) {
                popUpTo(Navigation.FeedScreen.navigationRoute) {
                    inclusive = false
                    this.saveState = true
                }
                launchSingleTop = true
                restoreState = true
            } },
            icon = { Icon(imageVector = Icons.Filled.Person, contentDescription = null)})
    }
}
