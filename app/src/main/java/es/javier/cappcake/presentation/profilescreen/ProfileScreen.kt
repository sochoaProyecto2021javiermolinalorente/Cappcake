package es.javier.cappcake.presentation.profilescreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.Divider
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.auth.FirebaseAuth
import es.javier.cappcake.R
import es.javier.cappcake.domain.Recipe
import es.javier.cappcake.domain.User
import es.javier.cappcake.presentation.Navigation
import es.javier.cappcake.presentation.components.ProfileImage
import es.javier.cappcake.presentation.components.RecipeComponent
import kotlinx.coroutines.CoroutineScope

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileScreenVIewModel, uid: String) {

    LaunchedEffect(key1 = Unit) {
        viewModel.loadUser(uid = uid)
        viewModel.loadRecipes(uid = uid)
    }

    BackHandler(enabled = true) {
        navController.popBackStack(Navigation.FeedScreen.navigationRoute, inclusive = false, true)
    }

    Scaffold(
        drawerContent = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Button(onClick = { signOut(navController = navController) }) {
                    Text(text = "Cerrar sesión".uppercase())
                }
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Box {
                Column(modifier = Modifier.padding(top = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    ProfileScreenProfileImage(
                        modifier = Modifier.size(90.dp),
                        profileImage = viewModel.user?.profileImage)

                    if (viewModel.user == null) {
                        Text(text = "Username", modifier = Modifier.padding(vertical = 10.dp))
                    } else {
                        Text(text = viewModel.user!!.username, modifier = Modifier.padding(vertical = 10.dp))
                    }

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp), verticalAlignment = Alignment.CenterVertically){
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Post")
                            Text(text = "1")
                        }

                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Following")
                            Text(text = "1")
                        }

                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Followers")
                            Text(text = "1")
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
                            loadUser = { viewModel.user }
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
    navController.clearBackStack(Navigation.APPLICATION_GRAPH)
    navController.navigate(navController.graph.findStartDestination().id)
}