package es.javier.cappcake.data.data_sources.recipe

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.javier.cappcake.data.data_sources.LoadImage
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
    private val updateRecipe: UpdateRecipe,
    private val getAllRecipes: GetAllRecipes,
    private val getRecipesOf: GetRecipesOf,
    private val getLastRecipe: GetLastRecipe,
    private val likeRecipe: LikeRecipe,
    private val unlikeRecipe: UnlikeRecipe,
    private val getRecipe: GetRecipe,
    private val getLikedRecipes: GetLikedRecipes,
    private val deleteRecipe: DeleteRecipe,
    private val loadImage: LoadImage
) {

    /**
     * Method to upload a recipe in firestore
     *
     * @param recipeName The recipe name of the recipe
     * @param recipeImageUri The recipe image of the recipe
     * @param recipeProcess The recipe process of the recipe
     * @param ingredients The list of ingredients of the recipe
     * @return The response with the status of the operation
     */
    suspend fun uploadRecipe(recipeName: String, recipeImageUri: Uri?, recipeProcess: String, ingredients: List<Ingredient>) : Response<Boolean> {
        return uploadRecipe.uploadRecipe(recipeName, recipeImageUri, recipeProcess, ingredients)
    }

    /**
     * Method to update the content of a recipe in firestore
     *
     * @param recipeId The id of the recipe to update
     * @param recipeName The new recipe name of the recipe
     * @param recipeImageUri The new recipe image of the recipe
     * @param recipeProcess The new recipe process of the recipe
     * @param ingredients The new list of ingredients of the recipe
     * @return The response with the status of the operation
     */
    suspend fun updateRecipe(recipeId: String, recipeName: String, recipeImageUri: Uri?, recipeProcess: String, ingredients: List<Ingredient>) : Response<Boolean> {
        return updateRecipe.updateRecipe(recipeId, recipeName, recipeImageUri, recipeProcess, ingredients)
    }

    /**
     * Method to to get an amount of recipes of a group of users in firestore
     *
     * @param uid The array of users ids
     * @param lastRecipeId The last id of the recipes collected by a previous call
     * @return The response with a list of the recipes and the id of the last recipe in the list
     */
    suspend fun getRecipesOf(uid: Array<String>, lastRecipeId: String?) : Response<Pair<List<Recipe>, String>> {
        return getRecipesOf.getRecipesOf(uid, lastRecipeId)
    }

    /**
     * Method to get an amount of all recipes recipes from firestore
     *
     * @param lastRecipeId The last recipeId collected by a previous call
     * @return The response with the list of recipes and the id of the last recipe in the list
     */
    suspend fun getAllRecipes(lastRecipeId: String?) : Response<Pair<List<Recipe>, String>> {
        return getAllRecipes.getAllRecipes(lastRecipeId)
    }

    /**
     * Method to get an amount of liked recipes by the current user in firestore
     *
     * @param lastRecipeId The last recipeId collected by a previous call
     * @return The response with the list of recipes and the id of the last recipe in the list
     */
    suspend fun getLikedRecipes(lastRecipeId: String?) : Response<Pair<List<Recipe>, String>> {
        return getLikedRecipes.getLikedRecipes(lastRecipeId)
    }

    /**
     * Method to get a recipe from firestore and if the current user has liked the recipe
     *
     * @param recipeId The id of the recipe
     * @return The response with the recipe and a boolean thar marks if the user has liked the
     * recipe or not
     */
    suspend fun getRecipe(recipeId: String) : Response<Pair<Recipe, Boolean>?> {
        return getRecipe.getRecipe(recipeId)
    }

    /**
     * Method to get the last recipe id of the last recipe uploaded by the current user
     *
     * @return The response with the id of the last recipe.
     */
    suspend fun getLastRecipe() : Response<String?> {
        return getLastRecipe.getLastRecipe()
    }

    /**
     * Method to load the image of a recipe
     *
     * @param path THe url path of the image
     * @return The response with the bitmap of the image
     */
    suspend fun loadRecipeImage(path: String) : Response<Bitmap?> {
        return loadImage.loadImage(path)
    }

    /**
     * Method to add the needed metadata thar represents the like of a user
     *
     * @param recipeId The id of the recipe to like
     * @return The response with status of the operation
     */
    suspend fun likeRecipe(recipeId: String) : Response<Boolean> {
        return likeRecipe.likeRecipe(recipeId)
    }

    /**
     * Method to delete the user id from the liked recipe in frestore
     *
     * @param recipeId The if of the recipe to unlike
     * @return The response with the status of the operation
     */
    suspend fun unlikeRecipe(recipeId: String) : Response<Boolean> {
        return unlikeRecipe.unlikeRecipe(recipeId)
    }

    /**
     * Method to delete a recipe document in firestore and its image in storage
     *
     * @param recipeId The id of the recipe to delete
     * @return The response with the status of the operation
     */
    suspend fun deleteRecipe(recipeId: String) : Response<Boolean> {
        return deleteRecipe.deleteRecipe(recipeId)
    }

}