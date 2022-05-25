package es.javier.cappcake.data.data_sources

import android.net.Uri
import com.google.firebase.firestore.Query
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
    private val getRecipesOf: GetRecipesOf
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

    suspend fun getRecipe(recipeId: String) : Response<Recipe?> {
        val ref = firestore.collection(FirebaseContracts.RECIPE_COLLECTION).document(recipeId)
        val query = ref.get(Source.SERVER)

        return suspendCoroutine { continuation ->
            query.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    val userId = document.getString(FirebaseContracts.RECIPE_USER_ID)
                    val recipeName = document.getString(FirebaseContracts.RECIPE_NAME)
                    val imagePath = document.getString(FirebaseContracts.RECIPE_IMAGE)
                    val recipeProcess = document.getString(FirebaseContracts.RECIPE_PROCESS)
                    val ingrediets = (document[FirebaseContracts.RECIPE_INGREDIENTS] as ArrayList<HashMap<String, Any>>).map {
                        val ingredientId = it[FirebaseContracts.INGREDIENT_ID] as String
                        val amount = it[FirebaseContracts.INGREDIENT_AMOUNT] as Double
                        val amountType = it[FirebaseContracts.INGREDIENT_AMOUNT_TYPE] as String
                        val name = it[FirebaseContracts.INGREDIENT_NAME] as String
                        Ingredient(id = ingredientId, name = name, amount = amount.toFloat(), amountType = AmountType.valueOf(amountType))
                    }
                    val recipe = Recipe(recipeId = recipeId, userId = userId!!, image = imagePath, ingredients = ingrediets, title = recipeName!!, recipeProcess = recipeProcess!!)
                    continuation.resume(Response.Success(data = recipe))
                } else {
                    continuation.resume(Response.Failiure(data = null, message = null))
                }
            }
        }
    }

}