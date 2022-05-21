package es.javier.cappcake.presentation.recipedetailscreen

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.javier.cappcake.domain.Ingredient
import es.javier.cappcake.presentation.ui.theme.notePageColor
import es.javier.cappcake.presentation.ui.theme.noteRedLine

@Composable
fun IngredientsTab(viewModel: RecipeDetailScreenViewModel) {
    
    if (viewModel.recipe == null) {
        
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        
    } else {

        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {
            Surface(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 10.dp, end = 10.dp), elevation = 4.dp, color = notePageColor) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    viewModel.recipe!!.ingredients.forEach {

                        if (viewModel.recipe!!.ingredients.first() != it) {
                            Divider(color = noteRedLine)
                        }

                        Ingredient(
                            modifier = Modifier.fillMaxWidth(),
                            ingredient = it
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
    

}

@Composable
fun Ingredient(modifier: Modifier, ingredient: Ingredient) {

    Column(modifier = modifier) {
        Row(modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp)) {
            Text(text = "- ${ingredient.name}: ${ingredient.amount} ${ingredient.amountType.name}", fontSize = 18.sp)
        }
    }

}