package es.javier.cappcake.domain

data class Comment(val commentId: String,
                   val userId: String,
                   val recipeId: String,
                   val commentDate: Long,
                   val comment: String)
