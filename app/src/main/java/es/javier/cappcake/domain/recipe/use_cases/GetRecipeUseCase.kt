package es.javier.cappcake.domain.recipe.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.recipe.Recipe
import es.javier.cappcake.domain.repositories.RecipeRepository
import javax.inject.Inject

class GetRecipeUseCase @Inject constructor(private val repository: RecipeRepository) {

    suspend operator fun invoke(recipeId: String) : Response<Pair<Recipe, Boolean>?> = repository.getRecipe(recipeId = recipeId)

}