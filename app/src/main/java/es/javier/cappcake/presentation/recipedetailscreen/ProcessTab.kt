package es.javier.cappcake.presentation.recipedetailscreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import es.javier.cappcake.presentation.ui.theme.notePageColor

@Composable
fun ProcessTab(viewModel: RecipeDetailScreenViewModel) {

    if (viewModel.recipe == null) {
        CircularProgressIndicator()
    } else {

        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {
            Surface(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 10.dp, end = 10.dp), elevation = 4.dp, color = notePageColor
            ) {
                Text(text = viewModel.recipe!!.recipeProcess, modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp))
            }

            Spacer(modifier = Modifier.height(50.dp))
        }

    }

}