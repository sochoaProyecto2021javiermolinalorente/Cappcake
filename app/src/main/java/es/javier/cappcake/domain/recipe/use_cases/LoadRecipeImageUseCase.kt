package es.javier.cappcake.domain.recipe.use_cases

import android.graphics.Bitmap
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.RecipeRepository
import javax.inject.Inject

class LoadRecipeImageUseCase @Inject constructor(private val repository: RecipeRepository) {

    suspend operator fun invoke(path: String) : Response<Bitmap?> = repository.loadRecipeImage(path)

}