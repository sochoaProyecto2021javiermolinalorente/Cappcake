package es.javier.cappcake.domain

import android.graphics.Bitmap

data class Recipe(val recipeId: String,
                  val userId: String,
                  val title: String,
                  val image: String? = null,
                  val recipeProcess: String,
                  val ingredients: List<Ingredient>)
