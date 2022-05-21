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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import es.javier.cappcake.R
import es.javier.cappcake.domain.AmountType
import es.javier.cappcake.domain.Ingredient
import es.javier.cappcake.presentation.utils.toFormattedFloat
import es.javier.cappcake.presentation.utils.toFormattedString

@Composable
fun IngredientsTab(viewModel: AddRecipeScreenViewModel) {
    LazyColumn(modifier = Modifier
        .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        items(viewModel.ingredients, key = { it.id }) { ingredient ->


            IngredientField(modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp), ingredient = ingredient) { viewModel.deleteIngredient(ingredient) }
        }

        item {
            IconButton(onClick = { }) {
                Icon(
                    modifier = Modifier.clickable { viewModel.addIngredient() },
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
            }
        }
    }
}

@Composable
fun IngredientField(modifier: Modifier = Modifier, ingredient: Ingredient, onDeleteClick: () -> Unit) {
    val focusManager = LocalFocusManager.current

    var ingredientName by remember { mutableStateOf(ingredient.name) }
    var ingredientAmount by remember { mutableStateOf(ingredient.amount.toFormattedString()) }

    Surface(modifier = modifier, shape = RoundedCornerShape(8.dp), elevation = 4.dp) {
        Row(modifier = Modifier.wrapContentSize().padding(2.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(id = R.string.add_recipe_ingredient_field_separator))
            Box(modifier = Modifier.wrapContentSize().padding(5.dp), contentAlignment = Alignment.CenterStart) {
                BasicTextField(
                    value = ingredientName,
                    onValueChange = {
                        if (it.length <= 30) {
                            ingredientName = it
                            ingredient.name = it
                        } },
                    modifier = Modifier.width(120.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Next) })
                )

                if (ingredientName.isBlank()) {
                    Text(text = stringResource(id = R.string.add_recipe_ingredient_name_hint), color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.width(4.dp))
            Text(text = stringResource(id = R.string.add_recipe_ingredient_field_separator))
            Spacer(modifier = Modifier.width(4.dp))
            BasicTextField(
                modifier = Modifier.width(40.dp),
                value = ingredientAmount,
                onValueChange = { value ->
                    try {

                        if (value.length > 4) return@BasicTextField

                        Log.i("formatter", "value: ${value.toFormattedFloat()}")
                        Log.i("formatter", value)
                        if (value.isEmpty()) {
                            ingredientAmount = "0"
                            ingredient.amount = 0f
                        } else {
                            if (ingredient.amount == 0f) {
                                ingredientAmount = value.replace("0", "")
                                ingredient.amount = ingredientAmount.toFormattedFloat()
                            } else {
                                ingredientAmount = value
                                ingredient.amount = ingredientAmount.toFormattedFloat()
                            }
                        }
                    } catch (exception: Exception) {}

                    Log.i("Amount", "${ingredient.amount}") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
            AmountTypeDropDownManeu(ingredient = ingredient)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.Delete,
                modifier = Modifier.clickable(onClick = onDeleteClick),
                contentDescription = null,
                tint = Color.Red)
        }
    }
}

@Composable
fun AmountTypeDropDownManeu(modifier: Modifier = Modifier, ingredient: Ingredient) {

    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(ingredient.amountType) }
    var items by remember { mutableStateOf(AmountType.values().filter { it != selectedItem }) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    Column(modifier = modifier) {
        Row(modifier = Modifier
            .onGloballyPositioned { coordinates ->
                textFieldSize = coordinates.size.toSize()
            }
            .clickable { expanded = !expanded }, verticalAlignment = Alignment.CenterVertically) {
            BasicText(
                text = selectedItem.name,
                modifier = Modifier
                    .width(80.dp))

            Icon(imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                contentDescription = null)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current) {
                textFieldSize.width.toDp()
            })) {

            items.forEach { amountType ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    selectedItem = amountType
                    ingredient.amountType = amountType}) {
                    items = AmountType.values().filter { it != selectedItem }
                    Text(text = amountType.name)
                }
            }

        }
    }

}
