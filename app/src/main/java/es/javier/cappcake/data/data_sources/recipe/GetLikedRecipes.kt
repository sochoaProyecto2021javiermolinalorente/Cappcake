package es.javier.cappcake.data.data_sources.recipe

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

class GetLikedRecipes @Inject constructor() {

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth

    suspend fun getLikedRecipes(lastRecipeId: String?) : Response<Pair<List<Recipe>, String>> {
        val recipesRef = firestore.collection(FirebaseContracts.LIKES_COLLECTION)

        val lastDocumentSnapshot: DocumentSnapshot? = if (lastRecipeId != null) {
            suspendCoroutine { continuation ->
                firestore.collection(FirebaseContracts.LIKES_COLLECTION).whereEqualTo(FirebaseContracts.LIKE_RECIPE_ID, lastRecipeId)
                    .get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(task.result.first())
                        } else {
                            continuation.resume(null)
                        }
                    }
            }
        } else {
            null
        }

        val query = if (lastDocumentSnapshot != null) {
            recipesRef.whereArrayContains(FirebaseContracts.LIKE_USERS, auth.uid!!)
                .orderBy(FirebaseContracts.LIKE_RECIPE_NAME)
                .startAfter(lastDocumentSnapshot).limit(10)
        } else {
            recipesRef.whereArrayContains(FirebaseContracts.LIKE_USERS, auth.uid!!)
                .orderBy(FirebaseContracts.LIKE_RECIPE_NAME)
                .limit(10)
        }


        return suspendCoroutine { continuation ->
            query.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val recipeList = task.result.documents.map { document ->
                        val recipeId = document.getString(FirebaseContracts.LIKE_RECIPE_ID)
                        val userId = document.getString(FirebaseContracts.LIKE_RECIPE_USER_ID)
                        val recipeName = document.getString(FirebaseContracts.LIKE_RECIPE_NAME)
                        val imagePath = document.getString(FirebaseContracts.LIKE_RECIPE_IMAGE)

                        Recipe(recipeId = recipeId!!, userId = userId!!, image = imagePath, ingredients = emptyList(), title = recipeName!!, recipeProcess = "")
                    }
                    if (recipeList.isEmpty()) {
                        continuation.resume(Response.Failiure(data = Pair(emptyList(), ""), message = null))
                    } else {
                        continuation.resume(Response.Success(data = Pair(recipeList, recipeList.last().recipeId)))
                    }
                } else {
                    continuation.resume(Response.Failiure(data = Pair(emptyList(), ""), message = null))
                }
            }
        }
    }

}