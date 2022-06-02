package es.javier.cappcake.data.data_sources.recipe

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Source
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

class GetRecipe @Inject constructor() {

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth

    suspend fun getRecipe(recipeId: String) : Response<Pair<Recipe, Boolean>?> {
        val ref = firestore.collection(FirebaseContracts.RECIPE_COLLECTION).document(recipeId)
        val query = ref.get(Source.SERVER)

        return suspendCoroutine { continuation ->

            firestore.runTransaction { transaction ->
                val recipe = transaction.get(ref)
                val likeRef = recipe.getDocumentReference(FirebaseContracts.RECIPE_LIKES_REF)

                val likeRecipeDocument = transaction.get(likeRef!!)
                val likedUsers = likeRecipeDocument[FirebaseContracts.LIKE_USERS] as List<*>
                val recipeLiked = likedUsers.contains(auth.uid!!)

                return@runTransaction Pair(recipe, recipeLiked)
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result.first
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

                    continuation.resume(Response.Success(data = Pair(recipe, task.result.second)))
                } else {
                    continuation.resume(Response.Failiure(data = null, throwable = task.exception))

                }

            }
        }
    }

}