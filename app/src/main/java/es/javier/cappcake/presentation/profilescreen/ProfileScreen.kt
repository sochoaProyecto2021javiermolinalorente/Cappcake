package es.javier.cappcake.presentation.profilescreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.auth.FirebaseAuth
import es.javier.cappcake.R
import es.javier.cappcake.presentation.Navigation
import es.javier.cappcake.presentation.components.RecipeComponent
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileScreenVIewModel, uid: String) {

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        viewModel.loadUser(uid = uid)
        viewModel.getFollowersCount(uid = uid)
        viewModel.loadRecipes(uid = uid)
    }

    if (viewModel.showUnFollowUserAlert.value) {
        UnfollowUserAlert(username = viewModel.user!!.username, showAlert = viewModel.showUnFollowUserAlert) {
            coroutineScope.launch {
                viewModel.showUnFollowUserAlert.value = false
                viewModel.unfollowUser(uid)
                viewModel.getFollowersCount(uid)
            }
        }
    }
    
    Scaffold(
        drawerContent = if (viewModel.getCurrentUserId() == uid) {
            {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Button(onClick = { signOut(navController = navController) }) {
                        Text(text = stringResource(id = R.string.profile_screen_sign_out_button_text).uppercase())
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
                            Text(text = stringResource(id = R.string.profile_screen_username_text), modifier = Modifier.padding(vertical = 10.dp))
                        } else {
                            Text(text = viewModel.user!!.username, modifier = Modifier.padding(vertical = 10.dp))
                        }

                        if (viewModel.getCurrentUserId() != uid) {
                            Spacer(modifier = Modifier.width(10.dp))
                            TextButton(onClick = {
                                coroutineScope.launch {
                                    if (viewModel.userFollowed) {
                                        viewModel.showUnFollowUserAlert.value = true
                                    } else {
                                        viewModel.followUser(uid)
                                    }
                                    viewModel.getFollowersCount(uid)
                                } }) {
                                Text(text = if (viewModel.userFollowed) stringResource(id = R.string.profile_screen_unfollow_button_text)
                                else stringResource(id = R.string.profile_screen_follow_button_text))
                            }
                        }
                    }

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp), verticalAlignment = Alignment.CenterVertically){
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = stringResource(id = R.string.profile_screen_posts_text))
                            Text(text = viewModel.user?.posts.toString() ?: "0")
                        }

                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = stringResource(id = R.string.profile_screen_following_text))
                            Text(text = viewModel.user?.following.toString() ?: "0")
                        }

                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = stringResource(id = R.string.profile_screen_followers_text))
                            Text(text = viewModel.followers?.toString() ?: "0")
                        }
                    }

                    Divider()
                }
            }

            if (viewModel.recipes != null) {
                if (viewModel.recipes!!.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {

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
                        Text(text = stringResource(id = R.string.profile_screen_no_recipes_text))
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Row {
                        Text(text = stringResource(id = R.string.profile_screen_loading_recipes_text))
                        Spacer(modifier = Modifier.width(10.dp))
                        CircularProgressIndicator()
                    }
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
fun UnfollowUserAlert(username: String, showAlert: MutableState<Boolean>, onConfirmClick: () -> Unit) {

    AlertDialog(
        onDismissRequest = { showAlert.value = false },
        title = {
                Text(text = stringResource(id = R.string.profile_screen_unfollow_user_alert_title))
        },
        text = {
               Text(text = buildAnnotatedString {
                   append(stringResource(id = R.string.profile_screen_unfollow_user_alert_message_text))
                   withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
                       append(" $username\n")
                   }
                   append(stringResource(id = R.string.profile_screen_unfollow_user_alert_question_text))
               })
        },
        confirmButton = {
            TextButton(onClick = onConfirmClick) {
                Text(text = stringResource(id = R.string.profile_screen_unfollow_user_alert_confirm_button_text), color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = { showAlert.value = false }) {
                Text(text = stringResource(id = R.string.profile_screen_unfollow_user_alert_cancel_button_text))
            }
        }
    )

}



private fun signOut(navController: NavController) {
    FirebaseAuth.getInstance().signOut()
    navController.clearBackStack(Navigation.FeedScreen.navigationRoute)
    navController.clearBackStack(Navigation.SearchScreen.navigationRoute)
    navController.clearBackStack(Navigation.AddRecipeScreen.navigationRoute)
    navController.clearBackStack(Navigation.ProfileScreen.navigationRoute)
    navController.navigate(navController.graph.findStartDestination().id)
}