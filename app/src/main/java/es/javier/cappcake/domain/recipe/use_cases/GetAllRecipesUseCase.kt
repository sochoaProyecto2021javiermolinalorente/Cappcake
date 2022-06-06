package es.javier.cappcake.domain.recipe.use_cases

import es.javier.cappcake.domain.recipe.Recipe
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.RecipeRepository
import javax.inject.Inject

class GetAllRecipesUseCase @Inject constructor(private val repository: RecipeRepository) {

    /**
     * Method to get an amount of all recipes
     *
     * @param lastRecipeId The last recipeId collected by a previous call
     * @return The response with the list of recipes and the id of the last recipe in the list
     */
    suspend operator fun invoke(lastRecipeId: String?) : Response<Pair<List<Recipe>, String>> = repository.getAllRecipes(lastRecipeId = lastRecipeId)

}