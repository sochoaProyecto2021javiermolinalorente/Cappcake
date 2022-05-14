package es.javier.cappcake.domain.use_cases

import android.net.Uri
import es.javier.cappcake.domain.Ingredient
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.RecipeRepository
import javax.inject.Inject

class UploadRecipeUseCase @Inject constructor(private val repository: RecipeRepository) {

    suspend operator fun invoke(recipeName: String,
                                recipeImage: Uri?,
                                recipeProcess: String,
                                ingredients: List<Ingredient>) : Response<Boolean> = repository.uploadRecipe(recipeName, recipeImage, recipeProcess, ingredients)

}