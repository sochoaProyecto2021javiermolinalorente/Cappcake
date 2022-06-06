package es.javier.cappcake.domain.recipe.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.recipe.Recipe
import es.javier.cappcake.domain.repositories.RecipeRepository
import javax.inject.Inject

class GetLikedRecipesUseCase @Inject constructor(private val repository: RecipeRepository) {

    /**
     * Method to get an amount of liked recipes by the current user
     *
     * @param lastRecipeId The last recipeId collected by a previous call
     * @return The response with the list of recipes and the id of the last recipe in the list
     */
    suspend operator fun invoke(lastRecipeId: String?) : Response<Pair<List<Recipe>, String>> = repository.getLikedRecipes(lastRecipeId)

}