package es.javier.cappcake.domain.recipe.use_cases

import android.graphics.Bitmap
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.repositories.RecipeRepository
import javax.inject.Inject

class LoadRecipeImageUseCase @Inject constructor(private val repository: RecipeRepository) {

    /**
     * Method to load the image of a recipe
     *
     * @param path THe url path of the image
     * @return The response with the bitmap of the image
     */
    suspend operator fun invoke(path: String) : Response<Bitmap?> = repository.loadRecipeImage(path)

}