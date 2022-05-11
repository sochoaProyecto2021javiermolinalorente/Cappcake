package es.javier.cappcake.presentation.addrecipescreen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import es.javier.cappcake.domain.AmountType
import es.javier.cappcake.domain.Ingredient
import es.javier.cappcake.presentation.Navigation
import es.javier.cappcake.presentation.ui.theme.CappcakeTheme
import es.javier.cappcake.presentation.utils.toFormattedFloat
import es.javier.cappcake.presentation.utils.toFormattedString
import java.lang.NumberFormatException
import java.text.DecimalFormat

@Composable
fun AddRecipeScreen(navController: NavController, viewModel: AddRecipeScreenViewModel) {

    var showIngredientsList by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {


            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(20.dp)) {
                Text(text = "-Nombre de la receta: ")
                BasicTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.recipeName,
                    onValueChange = { viewModel.recipeName = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    maxLines = 2)
            }

            Text(text = "Ingredientes")
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {

                items(viewModel.ingredients) { ingredient ->
                    IngredientField(ingredient = ingredient) { viewModel.deleteIngredient(ingredient) }
                }

                item {
                    IconButton(onClick = {  }) {
                        Icon(
                            modifier = Modifier.clickable { viewModel.addIngredient() },
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = null,
                            tint = Color.Black)
                    }
                }

                item {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        value = viewModel.recipeProcess,
                        onValueChange = { viewModel.recipeProcess = it },
                        label = { Text(text = "Process") }
                    )
                }

            }

            Button(
                onClick = { viewModel.uploadRecipe() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                shape = RoundedCornerShape(8.dp)) {
                Text(text = "Upload recipe")
            }

        }
    }
}
