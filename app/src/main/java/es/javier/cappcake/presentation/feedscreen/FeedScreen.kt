package es.javier.cappcake.presentation.feedscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import es.javier.cappcake.R
import es.javier.cappcake.domain.recipe.Recipe
import es.javier.cappcake.domain.user.User
import es.javier.cappcake.presentation.components.ProfileImage
import es.javier.cappcake.presentation.components.RecipeComponent
import es.javier.cappcake.presentation.ui.theme.CappcakeTheme

@Composable
fun FeedScreen(navController: NavController, viewModel: FeedScreenViewModel) {

    LaunchedEffect(key1 = Unit) {
        viewModel.loadFollowedUsers()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column {

            if (viewModel.users.isEmpty()) {
                Text(text = "No users followed")
            } else {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(viewModel.users, key = { it.userId }) {
                        UserFollowed(modifier = Modifier
                            .size(65.dp)
                            .padding(5.dp), user = it)
                    }
                }
            }
            Divider(color = Color.Black, thickness = 1.dp)
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(100) {
                RecipeComponent(
                    modifier = Modifier.padding(20.dp),
                    recipe = Recipe(recipeId = "", recipeProcess = "", ingredients = emptyList(), userId = "", title = "Unknown"),
                    loadUser = { User(profileImage = null, email = "unknown", username = "Unknown", userId = "", posts = 0, following = 0) }
                )
            }
        }
    }

}

@Composable
fun UserFollowed(modifier: Modifier, user: User) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colors.primary)
    ) {

        if (user.profileImage != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.profileImage)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop)
        } else {
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = null
            )
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