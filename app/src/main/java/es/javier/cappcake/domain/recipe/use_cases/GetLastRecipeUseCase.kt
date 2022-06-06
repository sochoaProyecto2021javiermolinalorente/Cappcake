package es.javier.cappcake.domain.recipe.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.RecipeRepository
import javax.inject.Inject

class GetLastRecipeUseCase @Inject constructor(private val repository: RecipeRepository) {

    /**
     * Method to get the last recipe id of the last recipe uploaded by the current user
     *
     * @return The response with the id of the last recipe.
     */
    suspend operator fun invoke() : Response<String?> =  repository.getLastRecipe()
}