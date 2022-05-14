package es.javier.cappcake.data.data_sources

import android.net.Uri
import dagger.hilt.android.scopes.ViewScoped
import es.javier.cappcake.domain.Ingredient
import es.javier.cappcake.domain.Response
import javax.inject.Inject

class RecipeDataSource @Inject constructor() {

    suspend fun uploadRecipe(recipeName: String, recipeImage: Uri?, recipeProcess: String, ingredients: List<Ingredient>) : Response<Boolean> {
        TODO("Implement firestore and firebase storage business logic")
    }

}