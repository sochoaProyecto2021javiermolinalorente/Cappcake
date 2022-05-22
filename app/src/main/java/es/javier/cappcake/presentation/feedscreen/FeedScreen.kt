package es.javier.cappcake.presentation.feedscreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import es.javier.cappcake.domain.recipe.Recipe
import es.javier.cappcake.domain.user.User
import es.javier.cappcake.presentation.components.ProfileImage
import es.javier.cappcake.presentation.components.RecipeComponent
import es.javier.cappcake.presentation.ui.theme.CappcakeTheme

@Composable
fun FeedScreen(navController: NavController, viewModel: FeedScreenViewModel) {

    Column(modifier = Modifier.fillMaxSize()) {
        Column {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(100) {
                    ProfileImage(
                        modifier = Modifier
                            .size(65.dp)
                            .padding(5.dp),
                        imagePath = null
                    )
                }
            }
            Divider(color = Color.Black, thickness = 1.dp)
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(100) {
                RecipeComponent(
                    modifier = Modifier.padding(20.dp),
                    recipe = Recipe(recipeId = "", recipeProcess = "", ingredients = emptyList(), userId = "", title = "Unknown"),
                    loadUser = { User(profileImage = null, email = "unknown", username = "Unknown", userId = "", posts = 0, following = 0, followers = 0) }
                )
            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FeedScreenPreview() {
    CappcakeTheme {
        FeedScreen(navController = NavController(LocalContext.current), viewModel = viewModel())
    }
}