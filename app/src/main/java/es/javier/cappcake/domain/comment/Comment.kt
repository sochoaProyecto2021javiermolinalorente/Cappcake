package es.javier.cappcake.domain.comment

import es.javier.cappcake.domain.user.User

data class Comment(val commentId: String,
                   val userId: String,
                   val recipeId: String,
                   val comment: String,
                   var user: User? = null)
