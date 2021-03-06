package es.javier.cappcake.presentation.searchscreen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
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
import es.javier.cappcake.R
import es.javier.cappcake.Navigation
import es.javier.cappcake.presentation.components.RecipeComponent
import es.javier.cappcake.utils.OnBottomReached
import es.javier.cappcake.utils.ScreenState
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(navController: NavController, viewModel: SearchScreenViewModel) {

    LaunchedEffect(key1 = Unit) {
        if (viewModel.screenStatus == ScreenState.LoadingData) {
            viewModel.loadAllRecipes()
        }
    }

    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    lazyListState.OnBottomReached {
        coroutineScope.launch {
            Log.i("Search_Screen", "loading more recipes")
            viewModel.lastRecipeId?.let { viewModel.loadMoreRecipes() }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(horizontal = 20.dp)) { navController.navigate(Navigation.SearchUserScreen.navigationRoute) }

        Divider(thickness = 1.dp, color = Color.Black)

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = viewModel.isRefreshing),
            onRefresh = {
                coroutineScope.launch { viewModel.loadRecipesAgain() }
            }) {

            if (viewModel.recipes.isNotEmpty()) {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.fillMaxSize()) {
                    items(viewModel.recipes, key = { it.recipeId }) {
                        RecipeComponent(
                            modifier = Modifier.padding(20.dp),
                            recipe = it,
                            loadUser = { viewModel.loadUser(it.userId) },
                            onUserClick = {
                                navController.navigate(Navigation.ProfileScreen.navigationRoute + "?userId=${it.userId}")
                            },
                            onRecipeClick = {
                                navController.navigate(Navigation.RecipeDetailScreen.navigationRoute + "?recipeId=${it.recipeId}")
                            }
                        )
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(id = R.string.search_screen_no_recipes_text))

                    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) { }

                }
            }
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier, onClick: () -> Unit) {

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = true, onClick = onClick),
            shape = CircleShape,
            elevation = 4.dp) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(5.dp)
                )
                Text(text = stringResource(id = R.string.search_search_hint), color = Color.Gray)
            }
        }
    }
}
