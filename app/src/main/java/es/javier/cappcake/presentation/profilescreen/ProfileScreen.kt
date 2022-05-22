package es.javier.cappcake.presentation.profilescreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.auth.FirebaseAuth
import es.javier.cappcake.R
import es.javier.cappcake.presentation.Navigation
import es.javier.cappcake.presentation.components.RecipeComponent

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileScreenVIewModel, uid: String) {

    LaunchedEffect(key1 = Unit) {
        viewModel.loadUser(uid = uid)
        viewModel.loadRecipes(uid = uid)
    }

    Scaffold(
        drawerContent = if (viewModel.getCurrentUserId() == uid) {
            {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Button(onClick = { signOut(navController = navController) }) {
                        Text(text = "Cerrar sesión".uppercase())
                    }
                }
            }
        } else null
    ) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Box {
                Column(modifier = Modifier.padding(top = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    ProfileScreenProfileImage(
                        modifier = Modifier.size(90.dp),
                        profileImage = viewModel.user?.profileImage)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (viewModel.user == null) {
                            Text(text = "Username", modifier = Modifier.padding(vertical = 10.dp))
                        } else {
                            Text(text = viewModel.user!!.username, modifier = Modifier.padding(vertical = 10.dp))
                        }

                        Spacer(modifier = Modifier.width(10.dp))
                        TextButton(onClick = { viewModel.followUser(uid) }) {
                            Text(text = if (viewModel.userFollowed) "Unfollow" else "Follow")
                        }
                    }

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp), verticalAlignment = Alignment.CenterVertically){
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Post")
                            Text(text = viewModel.user?.posts.toString() ?: "0")
                        }

                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Following")
                            Text(text = viewModel.user?.following.toString() ?: "0")
                        }

                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Followers")
                            Text(text = viewModel.user?.followers.toString() ?: "0")
                        }
                    }

                    Divider()
                }
            }

            if (viewModel.recipes != null) {
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)) {

                    items(viewModel.recipes!!, key = { it.recipeId }) {

                        RecipeComponent(
                            modifier = Modifier.padding(20.dp),
                            recipe = it,
                            loadUser = { viewModel.user },
                            onRecipeClick = { navController.navigate(Navigation.RecipeDetailScreen.navigationRoute + "?recipeId=${it.recipeId}") }
                        )
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun ProfileScreenProfileImage(modifier: Modifier, profileImage: String?) {
    Surface(modifier = modifier,
        shape = CircleShape,
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colors.primary)) {
        if (profileImage != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(profileImage)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop)

        } else {
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = null,
                contentScale = ContentScale.Crop)
        }

    }
}



private fun signOut(navController: NavController) {
    FirebaseAuth.getInstance().signOut()
    navController.clearBackStack(Navigation.FeedScreen.navigationRoute)
    navController.clearBackStack(Navigation.SearchScreen.navigationRoute)
    navController.clearBackStack(Navigation.AddRecipeScreen.navigationRoute)
    navController.clearBackStack(Navigation.ProfileScreen.navigationRoute)
    navController.navigate(navController.graph.findStartDestination().id)
}