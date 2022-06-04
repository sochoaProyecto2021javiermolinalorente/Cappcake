package es.javier.cappcake.domain.recipe

import es.javier.cappcake.domain.Ingredient
import es.javier.cappcake.domain.user.User

data class Recipe(val recipeId: String,
                  val userId: String,
                  val title: String,
                  val image: String? = null,
                  val recipeProcess: String,
                  val ingredients: List<Ingredient>,
                  var user: User? = null)
