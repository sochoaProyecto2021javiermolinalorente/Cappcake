package es.javier.cappcake.domain.activity

import es.javier.cappcake.domain.user.User

data class Activity(
    val activityId: String,
    val userId: String,
    val affectedUserId: String,
    val activityType: ActivityType,
    val recipeId: String?,
    var user: User? = null
)

enum class ActivityType {
    FOLLOW,
    LIKE,
    COMMENT
}
