package es.javier.cappcake.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import es.javier.cappcake.R
import es.javier.cappcake.domain.recipe.Recipe
import es.javier.cappcake.domain.user.User
import kotlinx.coroutines.CoroutineScope

@Composable
fun RecipeComponent(modifier: Modifier,
                    recipe: Recipe,
                    loadUser: suspend CoroutineScope.() -> User?,
                    onUserClick: () -> Unit = {},
                    onRecipeClick: () -> Unit = {}) {

    var user: User? by remember { mutableStateOf(null) }

    LaunchedEffect(key1 = Unit) {
        user = loadUser()
    }

    Surface(
        shape = RectangleShape,
        elevation = 5.dp,
        border = BorderStroke(width = 0.dp, color = Color.Gray),
        modifier = modifier.clickable(onClick = onRecipeClick)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onUserClick)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ProfileImage(
                        modifier = Modifier
                            .size(50.dp)
                            .padding(5.dp),
                        imagePath = user?.profileImage)
                    Text(
                        text = user?.username ?: "Username",
                        style = MaterialTheme.typography.body1,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Divider(color = Color.Black)
            }

            if (recipe.image != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(recipe.image)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp))

            } else {
                Image(
                    painter = painterResource(id = R.drawable.burgir),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alpha = 0.7f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            }

            Divider(thickness = 0.dp, color = Color.Black)

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(10.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}