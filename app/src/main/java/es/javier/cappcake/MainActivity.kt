@file:OptIn(ExperimentalPagerApi::class)

package es.javier.cappcake

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.javier.cappcake.presentation.activityscreen.ActivityScreen
import es.javier.cappcake.presentation.addrecipescreen.WriteRecipeScreen
import es.javier.cappcake.presentation.addrecipescreen.WriteRecipeScreenViewModel
import es.javier.cappcake.presentation.addrecipescreen.RecipeProcessScreen
import es.javier.cappcake.presentation.commentsscreen.CommentsScreen
import es.javier.cappcake.presentation.editprofilescreen.EditProfileScreen
import es.javier.cappcake.presentation.feedscreen.FeedScreen
import es.javier.cappcake.presentation.likedrecipesscreen.LikedRecipesScreen
import es.javier.cappcake.presentation.loadingscreen.LoadingScreen
import es.javier.cappcake.presentation.loginscreen.LoginScreen
import es.javier.cappcake.presentation.profilescreen.ProfileScreen
import es.javier.cappcake.presentation.recipedetailscreen.RecipeDetailScreen
import es.javier.cappcake.presentation.registerscreen.RegisterScreen
import es.javier.cappcake.presentation.searchscreen.SearchScreen
import es.javier.cappcake.presentation.searchscreen.SearchUserScreen
import es.javier.cappcake.presentation.ui.theme.CappcakeTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            navController = rememberNavController()

            val currentBackStackEntry by navController.currentBackStackEntryAsState()

            LaunchedEffect(key1 = currentBackStackEntry) {
                Log.i("Navigation", "Current destination: ${navController.currentDestination?.route}")
                if (currentBackStackEntry?.destination?.route == Navigation.RegisterScreen.navigationRoute ||
                    currentBackStackEntry?.destination?.route == Navigation.EditProfileScreen.navigationRoute) {
                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
                } else {
                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                }
            }

            CappcakeTheme {
                Scaffold(
                    bottomBar = {

                        when (currentBackStackEntry?.destination?.route) {
                            Navigation.FeedScreen.navigationRoute,
                            Navigation.SearchScreen.navigationRoute,
                            Navigation.WriteRecipeScreen.navigationRoute + "?recipeId={recipeId}",
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

        composable(Navigation.SearchUserScreen.navigationRoute) {
            SearchUserScreen(navController = navController, viewModel = hiltViewModel())
        }

        composable(
            "${Navigation.WriteRecipeScreen.navigationRoute}?recipeId={recipeId}",
            arguments = listOf(
                navArgument("recipeId") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            WriteRecipeScreen(
                navController = navController,
                viewModel = hiltViewModel(),
                recipeId = backStackEntry.arguments?.getString("recipeId")
            )
        }

        composable(Navigation.RecipeProcessScreen.navigationRoute) { backStackEntry ->
            val parentBackStackEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Navigation.WriteRecipeScreen.navigationRoute + "?recipeId={recipeId}")
            }
            val parentViewModel = hiltViewModel<WriteRecipeScreenViewModel>(parentBackStackEntry)

            RecipeProcessScreen(navController = navController, viewModel = parentViewModel)
        }

        composable(Navigation.ActivityScreen.navigationRoute) {
            ActivityScreen(navController = navController, viewModel = hiltViewModel())
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

        composable(Navigation.EditProfileScreen.navigationRoute) {
            EditProfileScreen(navController = navController, viewModel = hiltViewModel())
        }

        composable(Navigation.LikedRecipesScreen.navigationRoute) {
            LikedRecipesScreen(navController = navController, viewModel = hiltViewModel())
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

        composable(
            "${Navigation.CommentsScreen.navigationRoute}?recipeId={recipeId}?userId={userId}",
            arguments = listOf(navArgument(name = "recipeId") {
                type = NavType.StringType
                nullable = true
            }, navArgument(name = "userId") {
                type = NavType.StringType
                nullable = false
            })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")
            val owner = backStackEntry.arguments?.getString("userId")

            if (recipeId != null && owner != null) {
                CommentsScreen(navController = navController, viewModel = hiltViewModel(), recipeId = recipeId, owner = owner)

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
            selected = currentDestination?.hierarchy?.any { it.route == "${Navigation.WriteRecipeScreen.navigationRoute}?recipeId={recipeId}" } == true,
            onClick = { navController.navigate(Navigation.WriteRecipeScreen.navigationRoute) {
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
