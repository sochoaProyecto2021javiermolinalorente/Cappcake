package es.javier.cappcake.domain.recipe.use_cases

import es.javier.cappcake.domain.recipe.Recipe
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.RecipeRepository
import javax.inject.Inject

class GetRecipesOfUseCase @Inject constructor(private val repository: RecipeRepository) {

    suspend operator fun invoke(uid: Array<String>) : Response<List<Recipe>> = repository.getRecipesOf(uid = uid)

}