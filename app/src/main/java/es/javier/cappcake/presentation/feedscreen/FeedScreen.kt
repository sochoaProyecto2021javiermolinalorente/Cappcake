package es.javier.cappcake.presentation.feedscreen

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import es.javier.cappcake.R
import es.javier.cappcake.domain.Recipe
import es.javier.cappcake.domain.User
import es.javier.cappcake.presentation.Navigation
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
                    loadUser = { User(profileImage = null, email = "unknown", username = "Unknown", userId = "") }
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