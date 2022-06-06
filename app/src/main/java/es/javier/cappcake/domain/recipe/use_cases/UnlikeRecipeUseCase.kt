package es.javier.cappcake.domain.recipe.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.RecipeRepository
import javax.inject.Inject

class UnlikeRecipeUseCase @Inject constructor(private val repository: RecipeRepository) {

    /**
     * Method to remove the like of a recipe
     *
     * @param recipeId The if of the recipe to unlike
     * @return The response with the status of the operation
     */
    suspend operator fun invoke(recipeId: String) : Response<Boolean> = repository.unlikeRecipe(recipeId)

}