package es.javier.cappcake.presentation.addrecipescreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.javier.cappcake.domain.AmountType
import es.javier.cappcake.domain.Ingredient
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRecipeScreenViewModel @Inject constructor() : ViewModel() {

    var recipeName by mutableStateOf("")
    var recipeProcess by mutableStateOf("")
    val ingredients = mutableStateListOf(Ingredient(name = "", amount = 0f, amountType = AmountType.NONE))


    fun addIngredient() {
        ingredients.add(Ingredient(name = "", amount = 0f, amountType = AmountType.NONE))

    }

    fun deleteIngredient(ingredient: Ingredient) {
        ingredients.remove(ingredient)
    }

    fun uploadRecipe() {
        viewModelScope.launch {

        }
    }

}