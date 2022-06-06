package es.javier.cappcake.domain.recipe.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.RecipeRepository
import javax.inject.Inject

class LikeRecipeUseCase @Inject constructor(private val repository: RecipeRepository) {

    /**
     * Method to add the needed metadata that represents the like of a user
     *
     * @param recipeId The id of the recipe to like
     * @return The response with status of the operation
     */
    suspend operator fun invoke(recipeId: String) : Response<Boolean> = repository.likeRecipe(recipeId)

}