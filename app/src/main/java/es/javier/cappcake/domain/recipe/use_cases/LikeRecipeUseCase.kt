package es.javier.cappcake.domain.recipe.use_cases

import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.RecipeRepository
import javax.inject.Inject

class LikeRecipeUseCase @Inject constructor(private val repository: RecipeRepository) {

    suspend operator fun invoke(recipeId: String) : Response<Boolean> = repository.likeRecipe(recipeId)

}