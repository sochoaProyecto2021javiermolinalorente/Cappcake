package es.javier.cappcake.domain.recipe.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.RecipeRepository
import javax.inject.Inject

class GetLastRecipeUseCase @Inject constructor(private val repository: RecipeRepository) {

    suspend operator fun invoke() : Response<String?> =  repository.getLastRecipe()
}