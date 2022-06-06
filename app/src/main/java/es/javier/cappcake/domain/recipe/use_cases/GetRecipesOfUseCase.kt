package es.javier.cappcake.domain.recipe.use_cases

import es.javier.cappcake.domain.recipe.Recipe
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.RecipeRepository
import javax.inject.Inject

class GetRecipesOfUseCase @Inject constructor(private val repository: RecipeRepository) {

    /**
     * Method to to get an amount of recipes of a group of users
     *
     * @param uid The array of users ids
     * @param lastRecipeId The last id of the recipes collected by a previous call
     * @return The response with a list of the recipes and the id of the last recipe in the list
     */
    suspend operator fun invoke(uid: Array<String>, lastRecipeId: String?) : Response<Pair<List<Recipe>, String>> = repository.getRecipesOf(uid = uid, lastRecipeId)

}