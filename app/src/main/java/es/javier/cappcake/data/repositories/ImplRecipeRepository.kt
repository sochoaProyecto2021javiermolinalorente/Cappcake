package es.javier.cappcake.data.repositories

import android.net.Uri
import es.javier.cappcake.data.data_sources.RecipeDataSource
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
    ): Response<Boolean> = dataSource.uploadRecipe(recipeName, recipeImageUri, recipeProcess, ingredients)

    override suspend fun getRecipesOf(uid: String): Response<List<Recipe>?> = dataSource.getRecipesOf(uid)

    override suspend fun getAllRecipes(): Response<List<Recipe>?> = dataSource.getAllRecipes()

    override suspend fun getRecipe(recipeId: String): Response<Recipe?> = dataSource.getRecipe(recipeId = recipeId)
}
