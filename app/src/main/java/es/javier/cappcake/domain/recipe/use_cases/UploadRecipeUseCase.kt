package es.javier.cappcake.domain.recipe.use_cases

import android.net.Uri
import es.javier.cappcake.domain.Ingredient
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.RecipeRepository
import javax.inject.Inject

class UploadRecipeUseCase @Inject constructor(private val repository: RecipeRepository) {

    /**
     * Method to upload a recipe
     *
     * @param recipeName The recipe name of the recipe
     * @param recipeImage The recipe image of the recipe
     * @param recipeProcess The recipe process of the recipe
     * @param ingredients The list of ingredients of the recipe
     * @return The response with the status of the operation
     */
    suspend operator fun invoke(recipeName: String,
                                recipeImage: Uri?,
                                recipeProcess: String,
                                ingredients: List<Ingredient>) : Response<Boolean> = repository.uploadRecipe(recipeName, recipeImage, recipeProcess, ingredients)

}