package es.javier.cappcake.domain.activity

data class Activity(
    val activityId: String,
    val userId: String,
    val affectedUserId: String,
    val activityType: ActivityType,
    val recipeId: String?
)

enum class ActivityType {
    FOLLOW,
    LIKE,
    COMMENT
}
