package es.javier.cappcake

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
    object SearchUserScreen: Navigation("search_user_screen")

    // Add recipe navigation
    object WriteRecipeScreen: Navigation("write_recipe_screen")
    object RecipeProcessScreen: Navigation("recipe_process_screen")

    object ActivityScreen: Navigation("activity_screen")

    // Profile screen navigtion
    object ProfileScreen: Navigation("profile_screen")
    object EditProfileScreen: Navigation("edit_profile_screen")
    object LikedRecipesScreen : Navigation("liked_recipes_screen")

    object RecipeDetailScreen: Navigation("recipeDetailScreen")
    object CommentsScreen: Navigation("comments_screen")
}
