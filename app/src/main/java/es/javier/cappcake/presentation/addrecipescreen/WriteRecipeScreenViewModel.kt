package es.javier.cappcake.presentation.addrecipescreen

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.javier.cappcake.domain.AmountType
import es.javier.cappcake.domain.Ingredient
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.recipe.Recipe
import es.javier.cappcake.domain.recipe.use_cases.*
import es.javier.cappcake.utils.ImageCompressor
import es.javier.cappcake.utils.ScreenState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriteRecipeScreenViewModel @Inject constructor(
    private val compressor: ImageCompressor,
    private val getRecipeUseCase: GetRecipeUseCase,
    private val uploadRecipeUseCase: UploadRecipeUseCase,
    private val updateRecipeUseCase: UpdateRecipeUseCase,
    private val getLastRecipeUseCase: GetLastRecipeUseCase,
    private val loadRecipeImageUseCase: LoadRecipeImageUseCase
) : ViewModel() {

    var recipeToEdit: Recipe? by mutableStateOf(null)
    var recipeName by mutableStateOf("")
        private set
    var recipeImageUri: Uri? by mutableStateOf(null)
        private set
    var recipeImage: Bitmap? by mutableStateOf(null)
        private set
    var recipeProcess by mutableStateOf("")
    val ingredients = mutableStateListOf(Ingredient(name = "", amount = 0f, amountType = AmountType.NONE))
    var recipeFinished by mutableStateOf(false)
    var screenState: ScreenState by mutableStateOf(ScreenState.LoadingData)

    val showLoadingAlert = mutableStateOf(false)
    val showStoragePermissionAlert = mutableStateOf(false)
    val showInvalidRecipeAlert = mutableStateOf(false)
    val showNoChangesAlert = mutableStateOf(false)

    fun addIngredient() { ingredients.add(Ingredient(name = "", amount = 0f, amountType = AmountType.NONE)) }

    fun deleteIngredient(ingredient: Ingredient) { ingredients.remove(ingredient) }

    fun setRecipeTitle(newRecipeName: String) {
        if (newRecipeName.length >= 70) return
        else recipeName = newRecipeName
    }

    fun updateRecipeImage(imageUri: Uri) {
        viewModelScope.launch {
            recipeImageUri = imageUri
            recipeImage = compressor.comporessBitmap(quality = ImageCompressor.LOW_QUALITY, imageUri = imageUri)
        }
    }

    fun uploadRecipe() {
        viewModelScope.launch {
            showLoadingAlert.value = true
            if (checkValidRecipe()) {
                val response = uploadRecipeUseCase(recipeName = recipeName, recipeImage = recipeImageUri, ingredients = ingredients, recipeProcess = recipeProcess)

                when (response) {
                    is Response.Failiure -> {
                        showInvalidRecipeAlert.value = true
                        showLoadingAlert.value = false
                    }
                    is Response.Success -> {
                        showLoadingAlert.value = false
                        recipeFinished = true
                    }
                }
            } else {
                showInvalidRecipeAlert.value = true
                showLoadingAlert.value = false
            }
        }
    }

    fun updateRecipe(recipeId: String) {
        viewModelScope.launch {

            showLoadingAlert.value = true

            if (!checkValidRecipe()) {
                showInvalidRecipeAlert.value = true
                showLoadingAlert.value = false
                return@launch
            }

            if (!checkEditedRecipe()) {
                showNoChangesAlert.value = true
                showLoadingAlert.value = false
                return@launch
            }

            val response = updateRecipeUseCase(recipeId, recipeName, recipeImageUri, recipeProcess, ingredients)

            when (response) {
                is Response.Failiure -> {
                    showInvalidRecipeAlert.value = true
                    showLoadingAlert.value = false
                }
                is Response.Success -> {
                    showLoadingAlert.value = false
                    recipeFinished = true
                }
            }
        }
    }

    suspend fun loadRecipe(recipeId: String) {
        val response = getRecipeUseCase(recipeId)

        when (response) {
            is Response.Failiure -> { }
            is Response.Success -> {
                val recipe = response.data!!.first
                recipeToEdit = recipe
                recipeName = recipe.title
                recipeImageUri = Uri.parse(recipe.image)
                recipeImageUri?.let {
                    recipeImage = loadRecipeImageUseCase(it.toString()).data
                }
                ingredients.clear()
                val ingredientsToEdit = recipe.ingredients.map { Ingredient(id = it.id, name = it.name, amount = it.amount, amountType = it.amountType) }
                ingredients.addAll(ingredientsToEdit)
                recipeProcess = recipe.recipeProcess
                screenState = ScreenState.DataLoaded
            }
        }
    }

    suspend fun getLastRecipe() : String? = getLastRecipeUseCase().data

    private fun checkValidRecipe() : Boolean {
        return checkName() && checkImage() && checkIngredients() && checkRecipeProcess()
    }

    private fun checkIngredients() : Boolean {
        val invalidIngredients = ingredients.filter {
            it.amountType == AmountType.NONE ||
                    it.name.isBlank() ||
                    it.amount == 0f
        }

        return invalidIngredients.isEmpty()
    }

    private fun checkName() : Boolean = recipeName.isNotBlank()
    private fun checkImage() : Boolean = recipeImageUri != null
    private fun checkRecipeProcess() : Boolean = recipeProcess.isNotBlank()

    private fun checkEditedRecipe() : Boolean {
        if (recipeToEdit?.title != recipeName) return true
        if (recipeToEdit?.image != recipeImageUri.toString()) return true
        if (recipeToEdit?.recipeProcess != recipeProcess) return true
        if (recipeToEdit?.ingredients != ingredients.toList()) return true
        return false
    }

}
