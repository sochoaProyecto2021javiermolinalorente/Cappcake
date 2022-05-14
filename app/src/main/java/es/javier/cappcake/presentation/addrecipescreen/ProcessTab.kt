package es.javier.cappcake.presentation.addrecipescreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import es.javier.cappcake.R
import es.javier.cappcake.presentation.Navigation
import es.javier.cappcake.presentation.ui.theme.notePageColor

@Composable
fun ProcessTab(navController: NavController, viewModel: AddRecipeScreenViewModel) {

    Surface(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp)
        .clickable {
            navController.navigate(Navigation.RecipeProcessScreen.navigationRoute)
        },
        color = notePageColor,
        elevation = 4.dp) {

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, top = 10.dp, bottom = 10.dp, end = 10.dp)) {
            Text(text = stringResource(id = R.string.add_recipe_process_note_label))

            Text(text = viewModel.recipeProcess, modifier = Modifier
                .fillMaxSize())

        }
    }
}
