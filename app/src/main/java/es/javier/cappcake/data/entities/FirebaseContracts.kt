package es.javier.cappcake.data.entities

object FirebaseContracts {

    const val UNKNOWN = "Unknown"

    // User collection
    const val USER_COLLECTION = "users"
    const val USER_NAME = "username"
    const val USER_EMAIL = "email"
    const val USER_PROFILE_IMAGE = "profileImage"

    // Recipe collection
    const val RECIPE_COLLECTION = "recipes"
    const val RECIPE_USER_ID = "userId"
    const val RECIPE_NAME = "recipeName"
    const val RECIPE_IMAGE = "imagePath"
    const val RECIPE_INGREDIENTS = "ingredients"
    const val RECIPE_PROCESS = "recipeProcess"

    // Ingredients
    const val INGREDIENT_ID = "id"
    const val INGREDIENT_AMOUNT = "amount"
    const val INGREDIENT_AMOUNT_TYPE = "amountType"
    const val INGREDIENT_NAME = "name"

}