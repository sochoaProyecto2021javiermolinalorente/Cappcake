package es.javier.cappcake.domain.recipe

import es.javier.cappcake.domain.Ingredient

data class Recipe(val recipeId: String,
                  val userId: String,
                  val title: String,
                  val image: String? = null,
                  val recipeProcess: String,
                  val ingredients: List<Ingredient>)
