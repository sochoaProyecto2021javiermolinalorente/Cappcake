package es.javier.cappcake.data.data_sources.recipe

import android.net.Uri
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.javier.cappcake.data.entities.FirebaseContracts
import es.javier.cappcake.domain.AmountType
import es.javier.cappcake.domain.Ingredient
import es.javier.cappcake.domain.recipe.Recipe
import es.javier.cappcake.domain.Response
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RecipeDataSource @Inject constructor(
    private val uploadRecipe: UploadRecipe,
    private val getAllRecipes: GetAllRecipes,
    private val getRecipesOf: GetRecipesOf,
    private val getLastRecipe: GetLastRecipe,
    private val likeRecipe: LikeRecipe,
    private val unlikeRecipe: UnlikeRecipe,
    private val getRecipe: GetRecipe
) {
    
    private val firestore = Firebase.firestore

    suspend fun uploadRecipe(recipeName: String, recipeImageUri: Uri?, recipeProcess: String, ingredients: List<Ingredient>) : Response<Boolean> {
        return uploadRecipe.uploadRecipe(recipeName, recipeImageUri, recipeProcess, ingredients)
    }

    suspend fun getRecipesOf(uid: Array<String>, lastRecipeId: String?) : Response<Pair<List<Recipe>, String>> {
        return getRecipesOf.getRecipesOf(uid, lastRecipeId)
    }

    suspend fun getAllRecipes(lastRecipeId: String?) : Response<Pair<List<Recipe>, String>> {
        return getAllRecipes.getAllRecipes(lastRecipeId)
    }

    suspend fun getRecipe(recipeId: String) : Response<Pair<Recipe, Boolean>?> {
        return getRecipe.getRecipe(recipeId)
    }

    suspend fun getLastRecipe() : Response<String?> {
        return getLastRecipe.getLastRecipe()
    }

    suspend fun likeRecipe(recipeId: String) : Response<Boolean> {
        return likeRecipe.likeRecipe(recipeId)
    }

    suspend fun unlikeRecipe(recipeId: String) : Response<Boolean> {
        return unlikeRecipe.unlikeRecipe(recipeId)
    }

}