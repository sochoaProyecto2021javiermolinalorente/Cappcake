package es.javier.cappcake.data.repositories

import android.graphics.Bitmap
import android.net.Uri
import es.javier.cappcake.data.data_sources.recipe.RecipeDataSource
import es.javier.cappcake.domain.Ingredient
import es.javier.cappcake.domain.recipe.Recipe
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.RecipeRepository
import javax.inject.Inject

class ImplRecipeRepository @Inject constructor(private val dataSource: RecipeDataSource) : RecipeRepository {

    override suspend fun uploadRecipe(
        recipeName: String,
        recipeImageUri: Uri?,
        recipeProcess: String,
        ingredients: List<Ingredient>
    ): Response<Boolean> {
        return dataSource.uploadRecipe(recipeName, recipeImageUri, recipeProcess, ingredients)
    }

    override suspend fun updateRecipe(
        recipeId: String,
        recipeName: String,
        recipeImageUri: Uri?,
        recipeProcess: String,
        ingredients: List<Ingredient>
    ): Response<Boolean> {
        return dataSource.updateRecipe(recipeId, recipeName, recipeImageUri, recipeProcess, ingredients)
    }

    override suspend fun getRecipesOf(uid: Array<String>, lastRecipeId: String?): Response<Pair<List<Recipe>, String>> = dataSource.getRecipesOf(uid, lastRecipeId)

    override suspend fun getAllRecipes(lastRecipeId: String?): Response<Pair<List<Recipe>, String>> = dataSource.getAllRecipes(lastRecipeId = lastRecipeId)

    override suspend fun getLikedRecipes(lastRecipeId: String?): Response<Pair<List<Recipe>, String>> {
        return dataSource.getLikedRecipes(lastRecipeId)
    }

    override suspend fun getRecipe(recipeId: String): Response<Pair<Recipe, Boolean>?> = dataSource.getRecipe(recipeId = recipeId)

    override suspend fun getLastRecipe(): Response<String?> {
        return dataSource.getLastRecipe()
    }

    override suspend fun loadRecipeImage(path: String): Response<Bitmap?> {
        return dataSource.loadRecipeImage(path)
    }

    override suspend fun likeRecipe(recipeId: String): Response<Boolean> {
        return dataSource.likeRecipe(recipeId)
    }

    override suspend fun unlikeRecipe(recipeId: String): Response<Boolean> {
        return dataSource.unlikeRecipe(recipeId)
    }

    override suspend fun deleteRecipe(recipeId: String): Response<Boolean> {
        return dataSource.deleteRecipe(recipeId)
    }
}
