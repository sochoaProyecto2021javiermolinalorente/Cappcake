package es.javier.cappcake.presentation.profilescreen

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import es.javier.cappcake.R
import es.javier.cappcake.domain.Recipe
import es.javier.cappcake.presentation.components.ProfileImage
import es.javier.cappcake.presentation.components.RecipeComponent

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileScreenVIewModel, uid: String) {

    LaunchedEffect(key1 = Unit) {
        viewModel.loadProfileImage(uid = uid)
        viewModel.loadRecipes(uid = uid)
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
                        profileImage = viewModel.profileImageUri)

                    if (viewModel.username.isBlank()) {
                        Text(text = "Username", modifier = Modifier.padding(vertical = 10.dp))
                    } else {
                        Text(text = viewModel.username, modifier = Modifier.padding(vertical = 10.dp))
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

                        ProfileScreenRecipeComponent(
                            modifier = Modifier.padding(20.dp),
                            recipe = it,
                            userName = viewModel.username,
                            userImage = viewModel.profileImageUri
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

@Composable
fun ProfileScreenRecipeComponent(modifier: Modifier, recipe: Recipe, userName: String, userImage: String?) {
    Surface(
        shape = RectangleShape,
        elevation = 5.dp,
        border = BorderStroke(width = 0.dp, color = Color.Gray),
        modifier = modifier
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ProfileImage(
                        modifier = Modifier
                            .size(50.dp)
                            .padding(5.dp),
                        imagePath = userImage,
                        onClick = {})
                    Text(text = userName, style = MaterialTheme.typography.body1)
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
                    modifier = Modifier.padding(10.dp))
            }
        }
    }
}

private fun signOut(navController: NavController) {
    FirebaseAuth.getInstance().signOut()
    navController.navigate(navController.graph.findStartDestination().id)
}