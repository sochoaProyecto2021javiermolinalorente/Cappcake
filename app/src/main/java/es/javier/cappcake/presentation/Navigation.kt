package es.javier.cappcake.presentation

sealed class Navigation(val navigationRoute: String) {

    companion object {
        const val AUTHENTCATION_GRAPH = "authentication_graph"
        const val APPLICATION_GRAPH = "application_graph"

        // Arguments
        const val USER_LOGGED = "user_logged"
    }

    object LoadingScreen: Navigation("loading_screen")
    object LoginScreen: Navigation("login_screen")
    object RegisterScreen: Navigation("register_screen")
    object FeedScreen: Navigation("feed_screen")
    object SearchScreen: Navigation("search_screen")
    object AddRecipeScreen: Navigation("add_recipe_screen")
    object ActivityScreen: Navigation("activity_screen")
    object ProfileScreen: Navigation("profile_screen")
}