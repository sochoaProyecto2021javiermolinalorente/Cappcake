package es.javier.cappcake.domain.recipe.use_cases

import android.net.Uri
import es.javier.cappcake.domain.Ingredient
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.RecipeRepository
import javax.inject.Inject

class UpdateRecipeUseCase @Inject constructor(private val repository: RecipeRepository) {

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
    suspend operator fun invoke(recipeId: String, recipeName: String, recipeImageUri: Uri?, recipeProcess: String, ingredients: List<Ingredient>) : Response<Boolean> =
        repository.updateRecipe(recipeId, recipeName, recipeImageUri, recipeProcess, ingredients)

}