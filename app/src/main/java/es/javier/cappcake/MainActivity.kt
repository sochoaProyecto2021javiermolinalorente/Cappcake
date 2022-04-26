package es.javier.cappcake

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
                val navController = rememberNavController()
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
                    NavHost(navController = navController, startDestination = Navigation.APPLICATION_GRAPH) {
                        LoginGraph(navController = navController)
                        ApplicationGraph(navController = navController)
                    }
                }
            }
        }
    }
}

fun NavGraphBuilder.LoginGraph(navController: NavController) {
    navigation(startDestination = Navigation.LoginScreen.navigationRoute, route = Navigation.AUTHENTCATION_GRAPH) {
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
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                    this.saveState = true
                }
                launchSingleTop = true
                restoreState = true
            } },
            icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = null)})

        BottomNavigationItem(
            selected = currentDestination?.hierarchy?.any { it.route == Navigation.SearchScreen.navigationRoute } == true,
            onClick = { navController.navigate(Navigation.SearchScreen.navigationRoute) {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                    this.saveState = true
                }
                launchSingleTop = true
                restoreState = true
            } },
            icon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null)})

        BottomNavigationItem(
            selected = currentDestination?.hierarchy?.any { it.route == Navigation.AddRecipeScreen.navigationRoute } == true,
            onClick = { navController.navigate(Navigation.AddRecipeScreen.navigationRoute) {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive
                    this.saveState = true
                }
                launchSingleTop = true
                restoreState = true
            } },
            icon = { Icon(imageVector = Icons.Filled.Add, contentDescription = null)})

        BottomNavigationItem(
            selected = currentDestination?.hierarchy?.any { it.route == Navigation.ActivityScreen.navigationRoute } == true,
            onClick = { navController.navigate(Navigation.ActivityScreen.navigationRoute) {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive
                    this.saveState = true
                }
                launchSingleTop = true
                restoreState = true
            } },
            icon = { Icon(imageVector = Icons.Filled.Favorite, contentDescription = null)})

        BottomNavigationItem(
            selected = currentDestination?.hierarchy?.any { it.route == Navigation.ProfileScreen.navigationRoute } == true,
            onClick = { navController.navigate(Navigation.ProfileScreen.navigationRoute) {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive
                    this.saveState = true
                }
                launchSingleTop = true
                restoreState = true
            } },
            icon = { Icon(imageVector = Icons.Filled.Person, contentDescription = null)})
    }
}
