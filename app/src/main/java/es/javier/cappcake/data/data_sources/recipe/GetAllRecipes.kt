package es.javier.cappcake.data.data_sources.recipe

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.javier.cappcake.data.entities.FirebaseContracts
import es.javier.cappcake.domain.AmountType
import es.javier.cappcake.domain.Ingredient
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.recipe.Recipe
import java.lang.IllegalArgumentException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GetAllRecipes @Inject constructor() {

    private val firestore = Firebase.firestore

    suspend fun getAllRecipes(lastRecipeId: String?) : Response<Pair<List<Recipe>, String>> {
        val ref = firestore.collection(FirebaseContracts.RECIPE_COLLECTION)

        val lastDocumentSnapshot: DocumentSnapshot? = if (lastRecipeId != null) {
            suspendCoroutine { continuation ->
                ref.document(lastRecipeId)
                    .get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(task.result)
                        } else {
                            continuation.resume(null)
                        }
                    }
            }
        } else {
            null
        }

        val query = try {
            if (lastDocumentSnapshot != null) {
                ref.orderBy(FirebaseContracts.RECIPE_TIMESTAMP, Query.Direction.DESCENDING).startAfter(lastDocumentSnapshot).limit(10)
            } else {
                ref.orderBy(FirebaseContracts.RECIPE_TIMESTAMP, Query.Direction.DESCENDING).limit(10)
            }
        } catch (ex: IllegalArgumentException) {
            return Response.Failiure(data = Pair(emptyList(), ""), throwable = ex)
        }

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
                        continuation.resume(Response.Success(data = Pair(emptyList(), "")))
                    } else {
                        continuation.resume(Response.Success(data = Pair(recipeList, recipeList.last().recipeId)))
                    }
                } else {
                    continuation.resume(Response.Failiure(data = Pair(emptyList(), ""), throwable = task.exception))
                }
            }
        }
    }

}