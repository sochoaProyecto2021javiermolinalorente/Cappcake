package es.javier.cappcake.domain.recipe.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.RecipeRepository
import javax.inject.Inject

class DeleteRecipeUseCase @Inject constructor(private val repository: RecipeRepository) {

    /**
     * Method to delete a recipe
     *
     * @param recipeId The id of the recipe to delete
     * @return The response with the status of the operation
     */
    suspend operator fun invoke(recipeId: String) : Response<Boolean> = repository.deleteRecipe(recipeId)

}