package es.javier.cappcake.presentation.profilescreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.firebase.auth.FirebaseAuth
import es.javier.cappcake.R
import es.javier.cappcake.Navigation
import es.javier.cappcake.presentation.components.RecipeComponent
import es.javier.cappcake.presentation.ui.theme.primaryVariant
import es.javier.cappcake.presentation.ui.theme.primarydrawervariant
import es.javier.cappcake.presentation.ui.theme.redvariant
import es.javier.cappcake.utils.OnBottomReached
import es.javier.cappcake.utils.ScreenState
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileScreenViewModel, uid: String) {

    LaunchedEffect(key1 = Unit) {
        if (viewModel.screenStatus == ScreenState.LoadingData) {
            viewModel.loadUser(uid = uid)
            viewModel.getFollowersCount(uid = uid)
            viewModel.loadRecipes(uid = uid)
        }
    }

    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    lazyListState.OnBottomReached {
        coroutineScope.launch {
            viewModel.lastRecipeId?.let { viewModel.loadMoreRecipes(uid) }
        }
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

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    
    Scaffold(
        scaffoldState = rememberScaffoldState(drawerState = drawerState),
        drawerContent = if (viewModel.getCurrentUserId() == uid) {
            {
                DrawerProfileSettings(navController = navController)
            }
        } else null
    ) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Box {

                if (uid == viewModel.getCurrentUserId()) {
                    IconButton(onClick = {
                        coroutineScope.launch { drawerState.open() }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary
                        )
                    }
                } else {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }

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

            if (viewModel.user != null) {
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = viewModel.isRefreshing),
                    onRefresh = {
                        coroutineScope.launch {
                            viewModel.loadUser(uid)
                            //viewModel.recipes.clear()
                            viewModel.loadRecipesAgain(uid)
                        }
                    }
                ) {

                    if (viewModel.recipes.isNotEmpty()) {
                        LazyColumn(
                            state = lazyListState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {

                            items(viewModel.recipes, key = { it.recipeId }) {

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

                            Column(modifier = Modifier
                                .fillMaxSize()
                                .scrollable(rememberScrollState(), Orientation.Vertical)) {}
                        }
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

@Composable
fun DrawerProfileSettings(navController: NavController) {

    Column(modifier = Modifier.fillMaxWidth()) {

        Surface(
            modifier = Modifier
                .padding(5.dp)
                .clickable { },
            color = primarydrawervariant,
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = stringResource(id = R.string.profile_screen_liked_recipes_text).uppercase()
                )
            }
        }

        Surface(
            modifier = Modifier
                .padding(5.dp)
                .clickable { navController.navigate(Navigation.EditProfileScreen.navigationRoute) },
            color = primarydrawervariant,
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = stringResource(id = R.string.profile_screen_edit_profile_text).uppercase()
                )
            }
        }

        Surface(
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 5.dp)
                .clickable { signOut(navController) },
            color = redvariant,
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Logout,
                    contentDescription = null,
                    tint = Color.Red
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = stringResource(id = R.string.profile_screen_sign_out_text).uppercase(),
                    color = Color.Red
                )
            }
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