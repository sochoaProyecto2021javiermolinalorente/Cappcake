package es.javier.cappcake.presentation

sealed class Navigation(val navigationRoute: String) {

    companion object {
        const val AUTHENTCATION_GRAPH = "authentication_graph"
        const val APPLICATION_GRAPH = "application_graph"
    }

    object LoginScreen: Navigation("login_screen")
    object RegisterScreen: Navigation("register_screen")
    object FeedScreen: Navigation("login_screen")
    object SearchScreen: Navigation("search_screen")
    object AddRecipeScreen: Navigation("add_recipe_screen")
    object ActivityScreen: Navigation("activity_screen")
    object ProfileScreen: Navigation("profile_screen")
}

val items = listOf(
    Navigation.FeedScreen,
    Navigation.SearchScreen,
    Navigation.AddRecipeScreen,
    Navigation.ActivityScreen,
    Navigation.ProfileScreen)