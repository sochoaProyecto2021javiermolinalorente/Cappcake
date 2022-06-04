package es.javier.cappcake.presentation.activityscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import es.javier.cappcake.Navigation
import es.javier.cappcake.R
import es.javier.cappcake.domain.activity.Activity
import es.javier.cappcake.domain.activity.ActivityType
import es.javier.cappcake.domain.user.User
import es.javier.cappcake.presentation.ui.theme.CappcakeTheme
import es.javier.cappcake.utils.OnBottomReached
import es.javier.cappcake.utils.ScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ActivityScreen(navController: NavController, viewModel: ActivityScreenViewModel) {

    val lazyList = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        if (viewModel.screenState == ScreenState.LoadingData) {
            viewModel.loadActivities()
        }
    }

    lazyList.OnBottomReached {
        coroutineScope.launch {
            viewModel.lastActivityId?.let {
                viewModel.loadMoreActivities()
            }
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = viewModel.isRefreshing),
        onRefresh = { coroutineScope.launch { viewModel.loadActivitiesAgain() } }
    ) {

        if (viewModel.activities.isEmpty()) {

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = stringResource(id = R.string.activity_scree_no_recent_activity_text))

                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())) { }

            }

        } else {
            LazyColumn(
                state = lazyList,
                modifier = Modifier.fillMaxSize()
            ) {

                items(viewModel.activities, key = { it.activityId }) {
                    ActivityComponent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        activity = it,
                        onClick = {
                            if (it.recipeId == null)
                                navController.navigate(Navigation.ProfileScreen.navigationRoute + "?userId=${it.userId}")
                            else
                                navController.navigate(Navigation.RecipeDetailScreen.navigationRoute + "?recipeId=${it.recipeId}")
                        },
                        loadUser = { viewModel.loadUser(it.userId) }
                    )
                }
            }
        }

    }

}

@Composable
fun ActivityComponent(
    modifier: Modifier,
    activity: Activity,
    onClick: () -> Unit,
    loadUser: suspend CoroutineScope.() -> User?
) {

    var user by remember { mutableStateOf(activity.user) }

    LaunchedEffect(key1 = Unit) {
        if (user == null) {
            activity.user = loadUser()
            user = activity.user
        }
    }

    val text = remember {
        when (activity.activityType) {
            ActivityType.FOLLOW -> R.string.activity_screen_follow_text
            ActivityType.LIKE -> R.string.activity_screen_like_text
            ActivityType.COMMENT -> R.string.activity_screen_comment_text
        }
    }

    val icon = remember {
        when (activity.activityType) {
            ActivityType.FOLLOW -> Icons.Filled.Person
            ActivityType.LIKE -> Icons.Filled.Favorite
            ActivityType.COMMENT -> Icons.Filled.Message
        }
    }

    Surface(
        modifier = modifier.clickable(onClick = onClick),
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 10.dp),
                tint = MaterialTheme.colors.primary
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("${user?.username} " ?: "Username")
                    }
                    withStyle(style = SpanStyle()) {
                        append(stringResource(id = text))
                    }
                },
                modifier = Modifier.padding(5.dp)
            )
        }
    }

}
