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
import es.javier.cappcake.domain.recipe.use_cases.UploadRecipeUseCase
import es.javier.cappcake.utils.ImageCompressor
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRecipeScreenViewModel @Inject constructor(
    private val compressor: ImageCompressor,
    private val uploadRecipeUseCase: UploadRecipeUseCase
) : ViewModel() {

    var recipeName by mutableStateOf("Tacos")
        private set
    var recipeImageUri: Uri? by mutableStateOf(null)
        private set
    var recipeImage: Bitmap? by mutableStateOf(null)
        private set
    var recipeProcess by mutableStateOf("fg8awfiolasdcfhnaiolsdchn")
    val ingredients = mutableStateListOf(Ingredient(name = "Tacos", amount = 1f, amountType = AmountType.AMOUNT))
    var recipeFinished by mutableStateOf(false)

    var showLoadingAlert = mutableStateOf(false)
    var showStoragePermissionAlert = mutableStateOf(false)
    var showInvalidRecipeAlert = mutableStateOf(false)

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

}
