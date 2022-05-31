package es.javier.cappcake.domain.comment

data class Comment(val commentId: String,
                   val userId: String,
                   val recipeId: String,
                   val comment: String)
