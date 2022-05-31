package es.javier.cappcake.domain.repositories

import android.net.Uri
import es.javier.cappcake.domain.Ingredient
import es.javier.cappcake.domain.recipe.Recipe
import es.javier.cappcake.domain.Response

interface RecipeRepository {

    suspend fun uploadRecipe(
        recipeName: String,
        recipeImageUri: Uri?,
        recipeProcess: String,
        ingredients: List<Ingredient>) : Response<Boolean>
    suspend fun getRecipesOf(uid: Array<String>, lastRecipeId: String?) : Response<Pair<List<Recipe>, String>>
    suspend fun getAllRecipes(lastRecipeId: String?) : Response<Pair<List<Recipe>, String>>
    suspend fun getRecipe(recipeId: String) : Response<Recipe?>

    suspend fun getLastRecipe() : Response<String?>

    suspend fun likeRecipe(recipeId: String) : Response<Boolean>

}