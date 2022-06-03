package es.javier.cappcake.domain.repositories

import android.graphics.Bitmap
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
    suspend fun updateRecipe(
        recipeId: String,
        recipeName: String,
        recipeImageUri: Uri?,
        recipeProcess: String,
        ingredients: List<Ingredient>
    ) : Response<Boolean>
    suspend fun getRecipesOf(uid: Array<String>, lastRecipeId: String?) : Response<Pair<List<Recipe>, String>>
    suspend fun getAllRecipes(lastRecipeId: String?) : Response<Pair<List<Recipe>, String>>
    suspend fun getLikedRecipes(lastRecipeId: String?) : Response<Pair<List<Recipe>, String>>
    suspend fun getRecipe(recipeId: String) : Response<Pair<Recipe, Boolean>?>
    suspend fun getLastRecipe() : Response<String?>
    suspend fun loadRecipeImage(path: String) : Response<Bitmap?>
    suspend fun likeRecipe(recipeId: String) : Response<Boolean>
    suspend fun unlikeRecipe(recipeId: String) : Response<Boolean>
    suspend fun deleteRecipe(recipeId: String) : Response<Boolean>

}