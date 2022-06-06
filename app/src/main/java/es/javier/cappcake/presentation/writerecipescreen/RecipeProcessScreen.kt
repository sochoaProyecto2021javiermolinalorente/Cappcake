package es.javier.cappcake.presentation.writerecipescreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import es.javier.cappcake.R
import es.javier.cappcake.presentation.ui.theme.notePageColor

@Composable
fun RecipeProcessScreen(navController: NavController, viewModel: WriteRecipeScreenViewModel) {

    Scaffold(
        topBar = {
            TopAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(
                        onClick = { navController.popBackStack() },
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    }

                    Text(text = stringResource(id = R.string.recipe_process_appbar_title),
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .weight(1f),
                        color = Color.White)
                }
            }
        }) {

        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
            color = notePageColor,
            elevation = 4.dp) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, top = 10.dp, bottom = 10.dp, end = 10.dp)
            ) {
                Text(text = stringResource(id = R.string.write_recipe_process_note_label))

                BasicTextField(
                    modifier = Modifier
                        .fillMaxSize(),
                    value = viewModel.recipeProcess,
                    onValueChange = { viewModel.recipeProcess = it },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences)
                )

            }
        }
    }
}
