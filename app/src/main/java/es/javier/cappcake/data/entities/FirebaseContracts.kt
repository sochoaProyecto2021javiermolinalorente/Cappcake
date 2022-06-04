package es.javier.cappcake.data.entities

object FirebaseContracts {

    const val UNKNOWN = "Unknown"
    const val NUMBER_UNKNOWN = 0

    // Followers collection
    const val FOLLOWERS_COLLECTION = "followers"
    const val FOLLOWERS_USER_ID = "userId"
    const val FOLLOWERS_PROFILE_IMAGE = "profileImage"
    const val FOLLOWERS_USERS = "users"

    // User collection
    const val USER_COLLECTION = "users"
    const val USER_NAME = "username"
    const val USER_EMAIL = "email"
    const val USER_PROFILE_IMAGE = "profileImage"
    const val USER_POSTS = "posts"
    const val USER_FOLLOWING = "following"
    const val USER_FOLLOWERS_COUNTER_COLLECTION = "followersCounter"
    const val USER_FOLLOWERS_COUNTER = "counter"
    const val USER_FOLLOWER_COUNTERS = 5
    const val USER_PROFILE_IMAGE_REFERENCE = "/profile_image/profile-image.jpg"

    // Index collection
    const val INDEX_COLLECTION = "index"
    const val INDEX_USER_DOCUMENT = "user"
    const val INDEX_USERNAME_COLLECTION = "username"
    const val INDEX_USERNAME_USER_ID = "userId"

    // Recipe collection
    const val RECIPE_COLLECTION = "recipes"
    const val RECIPE_USER_ID = "userId"
    const val RECIPE_NAME = "recipeName"
    const val RECIPE_IMAGE = "imagePath"
    const val RECIPE_INGREDIENTS = "ingredients"
    const val RECIPE_PROCESS = "recipeProcess"
    const val RECIPE_TIMESTAMP = "recipeTimestamp"
    const val RECIPE_LIKES_REF = "recipeLikesRef"

    // Comments collection
    const val COMMENT_COLLECTION = "comments"
    const val COMMENT_RECIPE_ID = "recipeId"
    const val COMMENT_USER_ID = "userId"
    const val COMMENT_TIMESTAMP = "commentTimestamp"
    const val COMMENT_MESSAGE = "comment"

    // Recipe likes collection
    const val LIKES_COLLECTION = "recipeLikes"
    const val LIKE_RECIPE_IMAGE = "recipeImage"
    const val LIKE_RECIPE_USER_ID = "userId"
    const val LIKE_RECIPE_ID = "recipeId"
    const val LIKE_RECIPE_NAME = "recipeName"
    const val LIKE_USERS = "users"

    // Ingredients
    const val INGREDIENT_ID = "id"
    const val INGREDIENT_AMOUNT = "amount"
    const val INGREDIENT_AMOUNT_TYPE = "amountType"
    const val INGREDIENT_NAME = "name"

    // Activity collection
    const val ACTIVITY_COLLECTION = "activity"
    const val ACTIVITY_USER_ID = "userId"
    const val ACTIVITY_AFFECTED_USER_ID = "affectedUserId"
    const val ACTIVITY_RECIPE_ID = "recipeId"
    const val ACTIVITY_TYPE = "type"
    const val ACTIVITY_TIMESTAMP = "activityTimestamp"

    // Error codes
    const val PERMISSION_DENIED = 7

}