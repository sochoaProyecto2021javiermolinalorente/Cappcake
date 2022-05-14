package es.javier.cappcake.domain.repositories

import android.net.Uri
import es.javier.cappcake.domain.Ingredient
import es.javier.cappcake.domain.Response

interface RecipeRepository {

    suspend fun uploadRecipe(
        recipeName: String,
        recipeImage: Uri?,
        recipeProcess: String,
        ingredients: List<Ingredient>
    ) : Response<Boolean>

}