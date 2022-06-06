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

    /**
     * Method to upload a recipe
     *
     * @param recipeName The recipe name of the recipe
     * @param recipeImageUri The recipe image of the recipe
     * @param recipeProcess The recipe process of the recipe
     * @param ingredients The list of ingredients of the recipe
     * @return The response with the status of the operation
     */
    override suspend fun uploadRecipe(
        recipeName: String,
        recipeImageUri: Uri?,
        recipeProcess: String,
        ingredients: List<Ingredient>
    ): Response<Boolean> {
        return dataSource.uploadRecipe(recipeName, recipeImageUri, recipeProcess, ingredients)
    }

    /**
     * Method to update a recipe
     *
     * @param recipeId The id of the recipe to update
     * @param recipeName The new recipe name of the recipe
     * @param recipeImageUri The new recipe image of the recipe
     * @param recipeProcess The new recipe process of the recipe
     * @param ingredients The new list of ingredients of the recipe
     * @return The response with the status of the operation
     */
    override suspend fun updateRecipe(
        recipeId: String,
        recipeName: String,
        recipeImageUri: Uri?,
        recipeProcess: String,
        ingredients: List<Ingredient>
    ): Response<Boolean> {
        return dataSource.updateRecipe(recipeId, recipeName, recipeImageUri, recipeProcess, ingredients)
    }

    /**
     * Method to to get an amount of recipes of a group of users
     *
     * @param uid The array of users ids
     * @param lastRecipeId The last id of the recipes collected by a previous call
     * @return The response with a list of the recipes and the id of the last recipe in the list
     */
    override suspend fun getRecipesOf(uid: Array<String>, lastRecipeId: String?): Response<Pair<List<Recipe>, String>> = dataSource.getRecipesOf(uid, lastRecipeId)

    /**
     * Method to get an amount of all recipes
     *
     * @param lastRecipeId The last recipeId collected by a previous call
     * @return The response with the list of recipes and the id of the last recipe in the list
     */
    override suspend fun getAllRecipes(lastRecipeId: String?): Response<Pair<List<Recipe>, String>> = dataSource.getAllRecipes(lastRecipeId = lastRecipeId)

    /**
     * Method to get an amount of liked recipes by the current user
     *
     * @param lastRecipeId The last recipeId collected by a previous call
     * @return The response with the list of recipes and the id of the last recipe in the list
     */
    override suspend fun getLikedRecipes(lastRecipeId: String?): Response<Pair<List<Recipe>, String>> {
        return dataSource.getLikedRecipes(lastRecipeId)
    }

    /**
     * Method to get a recipe and if the current user has liked the recipe
     *
     * @param recipeId The id of the recipe
     * @return The response with the recipe and a boolean thar marks if the user has liked the
     * recipe or not
     */
    override suspend fun getRecipe(recipeId: String): Response<Pair<Recipe, Boolean>?> = dataSource.getRecipe(recipeId = recipeId)

    /**
     * Method to get the last recipe id of the last recipe uploaded by the current user
     *
     * @return The response with the id of the last recipe.
     */
    override suspend fun getLastRecipe(): Response<String?> {
        return dataSource.getLastRecipe()
    }

    /**
     * Method to load the image of a recipe
     *
     * @param path THe url path of the image
     * @return The response with the bitmap of the image
     */
    override suspend fun loadRecipeImage(path: String): Response<Bitmap?> {
        return dataSource.loadRecipeImage(path)
    }

    /**
     * Method to add the needed metadata that represents the like of a user
     *
     * @param recipeId The id of the recipe to like
     * @return The response with status of the operation
     */
    override suspend fun likeRecipe(recipeId: String): Response<Boolean> {
        return dataSource.likeRecipe(recipeId)
    }

    /**
     * Mehthod to remove the like of a recipe
     *
     * @param recipeId The if of the recipe to unlike
     * @return The response with the status of the operation
     */
    override suspend fun unlikeRecipe(recipeId: String): Response<Boolean> {
        return dataSource.unlikeRecipe(recipeId)
    }

    /**
     * Method to delete a recipe
     *
     * @param recipeId The id of the recipe to delete
     * @return The response with the status of the operation
     */
    override suspend fun deleteRecipe(recipeId: String): Response<Boolean> {
        return dataSource.deleteRecipe(recipeId)
    }
}
