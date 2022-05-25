package es.javier.cappcake.presentation.feedscreen

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import es.javier.cappcake.R
import es.javier.cappcake.domain.user.User
import es.javier.cappcake.presentation.Navigation
import es.javier.cappcake.presentation.components.RecipeComponent
import es.javier.cappcake.presentation.ui.theme.CappcakeTheme
import es.javier.cappcake.presentation.ui.theme.orangish
import es.javier.cappcake.utils.OnBottomReached
import es.javier.cappcake.utils.ScreenState
import kotlinx.coroutines.launch

@Composable
fun FeedScreen(navController: NavController, viewModel: FeedScreenViewModel) {

    LaunchedEffect(key1 = Unit) {
        if (viewModel.screenStatus == ScreenState.LoadingData) {
            viewModel.loadFollowedUsers()
            viewModel.loadRecipesOfFollowers()
        }
    }


    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    lazyListState.OnBottomReached {
        coroutineScope.launch {
            Log.i("feed_screen", "loading more recipes")
            viewModel.lastRecipeId?.let { viewModel.loadMoreRecipes() }
        }
    }


    Column(modifier = Modifier.fillMaxSize()) {
        Column {

            if (viewModel.users != null) {
                if (viewModel.users!!.isEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(70.dp)) {
                        Text(text = stringResource(id = R.string.feed_screen_no_users_text), modifier = Modifier
                            .fillMaxWidth(), textAlign = TextAlign.Center)
                    }
                } else {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(viewModel.users!!, key = { it.userId }) {

                            if (viewModel.userFilter == it.userId) {
                                SelectedUserFollowed(modifier = Modifier
                                    .size(65.dp)
                                    .padding(5.dp), user = it) {
                                    coroutineScope.launch {
                                        viewModel.userFilter = ""
                                        viewModel.loadRecipesOfFollowers()
                                    }
                                }
                            } else {
                                UserFollowed(modifier = Modifier
                                    .size(65.dp)
                                    .padding(5.dp), user = it) {
                                    coroutineScope.launch {
                                        viewModel.userFilter = it.userId
                                        viewModel.loadRecipesOfFollowers()
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .height(70.dp)
                    .fillMaxWidth()) {
                    Text(text = stringResource(id = R.string.feed_screen_loading_users_text))
                    Spacer(modifier = Modifier.width(10.dp))
                    CircularProgressIndicator()
                }
            }
            Divider(color = Color.Black, thickness = 1.dp)
        }

        if (viewModel.recipes != null) {
            if (viewModel.recipes!!.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(id = R.string.feed_screen_no_recipes_text))
                }
            } else {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.fillMaxSize()) {
                    items(viewModel.recipes!!, key = { it.recipeId }) {
                        RecipeComponent(
                            modifier = Modifier.padding(20.dp),
                            recipe = it,
                            loadUser = { viewModel.loadUser(it.userId) },
                            onUserClick = { navController.navigate(Navigation.ProfileScreen.navigationRoute + "?userId=${it.userId}") },
                            onRecipeClick = { navController.navigate(Navigation.RecipeDetailScreen.navigationRoute + "?recipeId=${it.recipeId}") }
                        )
                    }

                    if (viewModel.loadingMoreRecipes) {
                        item {
                            CircularProgressIndicator(modifier = Modifier.padding(vertical = 5.dp))
                        }
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Row {
                    Text(text = stringResource(id = R.string.feed_screen_loading_recipes_text))
                    Spacer(modifier = Modifier.width(10.dp))
                    CircularProgressIndicator()
                }
            }
        }
    }

}

@Composable
fun UserFollowed(modifier: Modifier, user: User, onClick: () -> Unit) {
    Surface(
        modifier = modifier.clickable(onClick = onClick)
    ) {

        if (user.profileImage != null) {
            AsyncImage(
                modifier = Modifier
                    .clip(CircleShape)
                    .border(width = 2.dp, color = MaterialTheme.colors.primary, shape = CircleShape),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.profileImage)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop)
        } else {
            Image(
                modifier = Modifier
                    .clip(CircleShape)
                    .border(width = 2.dp, color = MaterialTheme.colors.primary, shape = CircleShape),
                painter = painterResource(id = R.drawable.profile),
                contentDescription = null
            )
        }
    }
}

@Composable
fun SelectedUserFollowed(modifier: Modifier, user: User, onClick: () -> Unit) {
    Surface(
        modifier = modifier.clickable(onClick = onClick)
    ) {

        Box(contentAlignment = Alignment.Center) {
            if (user.profileImage != null) {
                AsyncImage(
                    modifier = Modifier
                        .clip(CircleShape)
                        .border(width = 2.dp, color = orangish, shape = CircleShape),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(user.profileImage)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop)
            } else {
                Image(
                    modifier = Modifier
                        .clip(CircleShape)
                        .border(width = 2.dp, color = orangish, shape = CircleShape),
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = null
                )
            }

            Icon(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .border(width = 2.dp, color = Color.White, shape = CircleShape),
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colors.primary
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
