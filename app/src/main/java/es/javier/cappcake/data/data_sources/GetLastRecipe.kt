package es.javier.cappcake.data.data_sources

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.javier.cappcake.data.entities.FirebaseContracts
import es.javier.cappcake.domain.AmountType
import es.javier.cappcake.domain.Ingredient
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.recipe.Recipe
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GetLastRecipe @Inject constructor() {

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth

    suspend fun getLastRecipe() : Response<String?> {
        val ref = firestore.collection(FirebaseContracts.RECIPE_COLLECTION)

        val query = ref.whereEqualTo(FirebaseContracts.RECIPE_USER_ID, auth.uid!!)
            .orderBy(FirebaseContracts.RECIPE_TIMESTAMP, Query.Direction.DESCENDING)
            .limit(1)

        return suspendCoroutine { continuation ->
            query.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val recipeList = task.result.documents.filter { it.exists() }.map { document ->
                        val recipeId = document.id
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
                        Recipe(recipeId = recipeId, userId = userId!!, image = imagePath, ingredients = ingrediets, title = recipeName!!, recipeProcess = recipeProcess!!)
                    }
                    if (recipeList.isEmpty()) {
                        continuation.resume(Response.Failiure(data = null, message = null))
                    } else {
                        continuation.resume(Response.Success(data = recipeList.first().recipeId))
                    }
                } else {
                    continuation.resume(Response.Failiure(data = null, message = null))
                }
            }
        }


    }

}