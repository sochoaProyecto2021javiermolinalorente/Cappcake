package es.javier.cappcake.domain.recipe.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.recipe.Recipe
import es.javier.cappcake.domain.repositories.RecipeRepository
import javax.inject.Inject

class GetRecipeUseCase @Inject constructor(private val repository: RecipeRepository) {

    /**
     * Method to get a recipe and if the current user has liked the recipe
     *
     * @param recipeId The id of the recipe
     * @return The response with the recipe and a boolean thar marks if the user has liked the
     * recipe or not
     */
    suspend operator fun invoke(recipeId: String) : Response<Pair<Recipe, Boolean>?> = repository.getRecipe(recipeId = recipeId)

}