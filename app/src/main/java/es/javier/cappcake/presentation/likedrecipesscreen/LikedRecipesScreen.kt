package es.javier.cappcake.presentation.likedrecipesscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import es.javier.cappcake.Navigation
import es.javier.cappcake.R
import es.javier.cappcake.presentation.components.RecipeComponent
import es.javier.cappcake.utils.OnBottomReached
import es.javier.cappcake.utils.ScreenState
import kotlinx.coroutines.launch

@Composable
fun LikedRecipesScreen(navController: NavController, viewModel: LikedRecipesScreenViewModel) {

    LaunchedEffect(key1 = Unit) {
        if (viewModel.screenState is ScreenState.LoadingData) {
            viewModel.loadRecipes()
        }
    }

    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    lazyListState.OnBottomReached {
        coroutineScope.launch {
            viewModel.lastRecipeId?.let { viewModel.loadMoreRecipes() }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.liked_recipes_screen_top_bar_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) {

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = viewModel.refreshing),
            onRefresh = { coroutineScope.launch {
                viewModel.loadRecipesAgain()
            } }
        ) {
            if (viewModel.recipes.isNotEmpty()) {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(viewModel.recipes, key = { it.recipeId }) {
                        RecipeComponent(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            recipe = it,
                            loadUser = { viewModel.loadUser(it.userId) },
                            onUserClick = {
                                if (viewModel.getCurrentId() == it.userId) {
                                    navController.popBackStack()
                                } else {
                                    navController.navigate(Navigation.ProfileScreen.navigationRoute + "?userId=${it.userId}")
                                } },
                            onRecipeClick = { navController.navigate(Navigation.RecipeDetailScreen.navigationRoute + "?recipeId=${it.recipeId}") }
                        )
                    }
                }
            } else {

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(id = R.string.liked_recipes_screen_no_recipes_loaded_text))
                }

            }
        }
    }

}