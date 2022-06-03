package es.javier.cappcake.domain.recipe.use_cases

import android.net.Uri
import es.javier.cappcake.domain.Ingredient
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.RecipeRepository
import javax.inject.Inject

class UpdateRecipeUseCase @Inject constructor(private val repository: RecipeRepository) {

    suspend operator fun invoke(recipeId: String, recipeName: String, recipeImageUri: Uri?, recipeProcess: String, ingredients: List<Ingredient>) : Response<Boolean> =
        repository.updateRecipe(recipeId, recipeName, recipeImageUri, recipeProcess, ingredients)

}