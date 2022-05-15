package es.javier.cappcake.data.repositories

import android.net.Uri
import es.javier.cappcake.data.data_sources.RecipeDataSource
import es.javier.cappcake.domain.Ingredient
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
}