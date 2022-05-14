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
import es.javier.cappcake.utils.ImageCompressor
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRecipeScreenViewModel @Inject constructor(
    private val compressor: ImageCompressor
) : ViewModel() {

    var recipeName by mutableStateOf("")
        private set
    var recipeImageUri: Uri? by mutableStateOf(null)
        private set
    var recipeImage: Bitmap? by mutableStateOf(null)
        private set
    var recipeProcess by mutableStateOf("")
    val ingredients = mutableStateListOf(Ingredient(name = "", amount = 0f, amountType = AmountType.NONE))

    var showStoragePermissionAlert = mutableStateOf(false)


    fun addIngredient() {
        ingredients.add(Ingredient(name = "", amount = 0f, amountType = AmountType.NONE))

    }

    fun deleteIngredient(ingredient: Ingredient) {
        ingredients.remove(ingredient)
    }

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

}
