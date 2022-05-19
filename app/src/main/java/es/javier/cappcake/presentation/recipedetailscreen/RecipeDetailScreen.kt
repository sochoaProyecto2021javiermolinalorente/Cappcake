package es.javier.cappcake.presentation.recipedetailscreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.*
import es.javier.cappcake.R
import kotlinx.coroutines.launch

private const val INGREDIENTS_TAB = 0
private const val PROCESS_TAB = 1
private const val TABS = 2

@OptIn(ExperimentalPagerApi::class)
@Composable
fun RecipeDetailScreen(navController: NavController, viewModel: RecipeDetailScreenViewModel, recipeId: String) {

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        viewModel.loadRecipe(recipeId)
    }

    if (viewModel.recipe == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }
    } else {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .crossfade(true)
                    .data(viewModel.recipe!!.image)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize(0.25f))

            Divider()
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = viewModel.recipe!!.title,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 20.dp),
                    style = MaterialTheme.typography.h5)
                Icon(
                    imageVector = Icons.Filled.FavoriteBorder,
                    contentDescription = null,
                    modifier = Modifier.padding(20.dp)
                )
                Icon(
                    imageVector = Icons.Filled.Comment,
                    contentDescription = null,
                    modifier = Modifier.padding(top = 20.dp, bottom = 20.dp, end = 20.dp))
            }
            Divider()

            TabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(modifier = Modifier.pagerTabIndicatorOffset(pagerState = pagerState, tabPositions = tabPositions))
                },
                backgroundColor = Color.White) {

                Tab(selected = pagerState.currentPage == INGREDIENTS_TAB,
                    onClick = { scope.launch { pagerState.animateScrollToPage(INGREDIENTS_TAB) } },
                    text = { Text(text = stringResource(id = R.string.recipe_detail_screen_ingredients_tab_label).uppercase()) })

                Tab(selected = pagerState.currentPage == PROCESS_TAB,
                    onClick = { scope.launch { pagerState.animateScrollToPage(PROCESS_TAB) } },
                    text = { Text(text = stringResource(id = R.string.recipe_detail_screen_process_tab_label).uppercase()) })

            }

            RecipeDetailScreenTabContent(pagerState = pagerState, viewModel = viewModel)

        }
    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun RecipeDetailScreenTabContent(pagerState: PagerState, viewModel: RecipeDetailScreenViewModel) {
    HorizontalPager(count = TABS, state = pagerState) { page ->
        when (page) {
            INGREDIENTS_TAB -> IngredientsTab(viewModel = viewModel)
            PROCESS_TAB -> ProcessTab(viewModel = viewModel)
        }
    }
}