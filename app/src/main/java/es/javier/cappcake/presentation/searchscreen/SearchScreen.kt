package es.javier.cappcake.presentation.searchscreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import es.javier.cappcake.R
import es.javier.cappcake.presentation.Navigation
import es.javier.cappcake.presentation.components.RecipeComponent

@Composable
fun SearchScreen(navController: NavController, viewModel: SearchScreenViewModel) {

    LaunchedEffect(key1 = Unit) {
        viewModel.loadAllRecipes()
    }

    BackHandler(enabled = true) {
        navController.popBackStack(Navigation.FeedScreen.navigationRoute, inclusive = false, true)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(horizontal = 20.dp)) {}

        Divider(thickness = 1.dp, color = Color.Black)

        if (viewModel.recipes.isNotEmpty()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
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
                CircularProgressIndicator()
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
