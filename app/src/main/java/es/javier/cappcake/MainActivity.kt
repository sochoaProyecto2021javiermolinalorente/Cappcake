@file:OptIn(ExperimentalPagerApi::class)

package es.javier.cappcake

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import es.javier.cappcake.presentation.Navigation
import es.javier.cappcake.presentation.activityscreen.ActivityScreen
import es.javier.cappcake.presentation.addrecipescreen.AddRecipeScreen
import es.javier.cappcake.presentation.addrecipescreen.AddRecipeScreenViewModel
import es.javier.cappcake.presentation.addrecipescreen.RecipeProcessScreen
import es.javier.cappcake.presentation.feedscreen.FeedScreen
import es.javier.cappcake.presentation.feedscreen.FeedScreenViewModel
import es.javier.cappcake.presentation.loadingscreen.LoadingScreen
import es.javier.cappcake.presentation.loginscreen.LoginScreen
import es.javier.cappcake.presentation.profilescreen.ProfileScreen
import es.javier.cappcake.presentation.profilescreen.ProfileScreenViewModel
import es.javier.cappcake.presentation.recipedetailscreen.RecipeDetailScreen
import es.javier.cappcake.presentation.registerscreen.RegisterScreen
import es.javier.cappcake.presentation.searchscreen.SearchScreen
import es.javier.cappcake.presentation.searchscreen.SearchScreenViewModel
import es.javier.cappcake.presentation.ui.theme.CappcakeTheme
import java.lang.Exception

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        const val IP_ADDRESS = "10.0.2.2"
    }

    private lateinit var navController: NavHostController

    init {
        try {

            FirebaseAuth.getInstance().useEmulator(IP_ADDRESS, 9099)
            FirebaseFirestore.getInstance().useEmulator(IP_ADDRESS, 8080)
            Firebase.firestore.firestoreSettings = firestoreSettings {
                isPersistenceEnabled = false
            }
            FirebaseStorage.getInstance().useEmulator(IP_ADDRESS, 9199)
        } catch (e: Exception) { }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                            Navigation.ActivityScreen.navigationRoute ->
                                BottomNavigationitems(
                                    navController = navController,
                                    currentBackStackEntry = currentBackStackEntry
                                )
                            "${Navigation.ProfileScreen.navigationRoute}?userId={userId}" -> {
                                if (currentBackStackEntry?.arguments?.getString("userId") == Firebase.auth.currentUser?.uid) {
                                    BottomNavigationitems(
                                        navController = navController,
                                        currentBackStackEntry = currentBackStackEntry
                                    )
                                }
                            }
                        }
                    }) { innerPadding ->
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = Navigation.LoadingScreen.navigationRoute) {
                        composable(route = Navigation.LoadingScreen.navigationRoute) { backStackEntry ->
                            LoadingScreen(navController = navController, viewModel = hiltViewModel())
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
            FeedScreen(navController = navController, viewModel = hiltViewModel())
        }
        composable(Navigation.SearchScreen.navigationRoute) {
            SearchScreen(navController = navController, viewModel = hiltViewModel())
        }
        composable(Navigation.AddRecipeScreen.navigationRoute) {
            AddRecipeScreen(navController = navController, hiltViewModel())
        }

        composable(Navigation.RecipeProcessScreen.navigationRoute) { backStackEntry ->
            val parentBackStackEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Navigation.AddRecipeScreen.navigationRoute)
            }
            val parentViewModel = hiltViewModel<AddRecipeScreenViewModel>(parentBackStackEntry)

            RecipeProcessScreen(navController = navController, viewModel = parentViewModel)
        }

        composable(Navigation.ActivityScreen.navigationRoute) {
            ActivityScreen(navController = navController)
        }
        composable("${Navigation.ProfileScreen.navigationRoute}?userId={userId}", arguments = listOf(navArgument(name = "userId") {
            type = NavType.StringType
            defaultValue = Firebase.auth.uid
            nullable = true
        })) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            userId?.let {
                ProfileScreen(navController = navController, viewModel = hiltViewModel(), uid = it)
            }
        }

        composable("${Navigation.RecipeDetailScreen.navigationRoute}?recipeId={recipeId}", arguments = listOf(
            navArgument(name = "recipeId") {
                type = NavType.StringType
                nullable = true
            })) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")
            recipeId?.let {
                RecipeDetailScreen(navController = navController, viewModel = hiltViewModel(), recipeId = it)
            }
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
                    this.saveState = false
                }
                launchSingleTop = true
                restoreState = false
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
            selected = currentDestination?.hierarchy?.any { it.route == "${Navigation.ProfileScreen.navigationRoute}?userId={userId}" } == true,
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
