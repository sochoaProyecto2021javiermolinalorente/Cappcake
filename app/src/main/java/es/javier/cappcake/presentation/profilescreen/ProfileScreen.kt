package es.javier.cappcake.presentation.profilescreen

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import es.javier.cappcake.presentation.components.RecipeComponent

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileScreenVIewModel, uid: String) {

    LaunchedEffect(key1 = Unit) {
        viewModel.loadProfileImage(uid = uid)
    }

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

                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
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

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)) {
            items(100) {
                RecipeComponent(modifier = Modifier.padding(20.dp), title = "Hamburguesa con queso", userName = "User_cooker123") {}
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
    /*Firebase.auth.currentUser?.delete()
        ?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navController.navigate(navController.graph.findStartDestination().id)
            }
        }*/

    FirebaseAuth.getInstance().signOut()
    navController.navigate(navController.graph.findStartDestination().id)

}