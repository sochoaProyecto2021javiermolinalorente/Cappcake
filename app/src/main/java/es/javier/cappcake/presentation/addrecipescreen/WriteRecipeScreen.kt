package es.javier.cappcake.presentation.addrecipescreen

import android.Manifest
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.*
import es.javier.cappcake.R
import es.javier.cappcake.Navigation
import es.javier.cappcake.presentation.components.ErrorDialog
import es.javier.cappcake.presentation.components.LoadingAlert
import es.javier.cappcake.presentation.components.StoragePermissionNotGrantedAlert
import es.javier.cappcake.presentation.ui.theme.notePageColor
import es.javier.cappcake.presentation.ui.theme.primary
import es.javier.cappcake.utils.ScreenState
import kotlinx.coroutines.launch

private const val INGREDIENTS_TAB = 0
private const val PROCESS_TAB = 1
private const val TABS = 2

@ExperimentalPagerApi
@Composable
fun WriteRecipeScreen(navController: NavController, viewModel: WriteRecipeScreenViewModel, recipeId: String?) {

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = viewModel.recipeFinished) {

        if (recipeId != null) {
            if (viewModel.screenState is ScreenState.LoadingData)
                viewModel.loadRecipe(recipeId)
        }

        if (viewModel.recipeFinished) {
            if (recipeId == null) {
                val id = viewModel.getLastRecipe()
                id?.let {
                    navController.navigate(Navigation.RecipeDetailScreen.navigationRoute + "?recipeId=${it}") {
                        popUpTo(Navigation.FeedScreen.navigationRoute)
                    }
                }
            } else {
                navController.popBackStack()
            }
        }
    }

    val imageSelector = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { imageUri ->
        imageUri?.let {
            viewModel.updateRecipeImage(it)
        }
    }

    val storagePermission = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            imageSelector.launch("image/*")
        } else {
            viewModel.showStoragePermissionAlert.value = true
        }
    }

    if (viewModel.showStoragePermissionAlert.value) {
        StoragePermissionNotGrantedAlert(viewModel.showStoragePermissionAlert)
    }

    if (viewModel.showLoadingAlert.value) {
        LoadingAlert(
            title = null,
            text = {
                Text(
                    text = stringResource(
                        id = if (recipeId == null) {
                            R.string.write_recipe_uploading_recipe_text
                        } else {
                            R.string.write_recipe_editing_recipe_Text
                        }
                    )
                )
            }
        )
    }

    if (viewModel.showInvalidRecipeAlert.value) {
        ErrorDialog(
            showDialog = viewModel.showInvalidRecipeAlert,
            title = R.string.write_recipe_not_valid_alert_title,
            text = R.string.write_recipe_not_valid_alert_text)
    }

    if (viewModel.showNoChangesAlert.value) {
        ErrorDialog(
            showDialog = viewModel.showNoChangesAlert,
            title = R.string.write_recipe_screen_no_changes_alert_title,
            text = R.string.write_reciperecipe_scree_no_changes_text
        )
    }

    Scaffold(topBar = {
        TopAppBar {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(
                        id = if (recipeId == null) {
                            R.string.write_recipe_screen_add_title
                        } else {
                            R.string.write_recipe_screen_edit_title
                        }
                    ),
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .weight(1f),
                    color = Color.White)

                IconButton(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    onClick = {
                        if (recipeId == null) {
                            viewModel.uploadRecipe()
                        } else {
                            viewModel.updateRecipe(recipeId)
                        } },
                ) {
                    Icon(imageVector = Icons.Filled.Save, contentDescription = null, tint = Color.White)
                }
            }
        }
    }) {
        Column(modifier = Modifier.fillMaxSize()) {

            RecipeNameField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                value = viewModel.recipeName,
                onNameChange = { viewModel.setRecipeTitle(it) })

            if (viewModel.recipeImageUri == null) {
                UploadRecipeImageField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(vertical = 5.dp, horizontal = 20.dp)) { storagePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE) }
            } else {
                ImageSelectedView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp, horizontal = 20.dp),
                    image = viewModel.recipeImage,
                    viewModel = viewModel,
                    onClick = { storagePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE) })
            }

            TabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(modifier = Modifier.pagerTabIndicatorOffset(pagerState, tabPositions))
                },
                backgroundColor = Color.White) {

                Tab(selected = pagerState.currentPage == INGREDIENTS_TAB,
                    onClick = { scope.launch { pagerState.animateScrollToPage(INGREDIENTS_TAB) } },
                    text = { Text(text = stringResource(id = R.string.write_recipe_ingredients_tab_label).uppercase(),
                        color = MaterialTheme.colors.onSurface) })

                Tab(selected = pagerState.currentPage == PROCESS_TAB,
                    onClick = { scope.launch { pagerState.animateScrollToPage(PROCESS_TAB) } },
                    text = { Text(text = stringResource(id = R.string.write_recipe_process_tab_label).uppercase(),
                        color = MaterialTheme.colors.onSurface) })


            }

            AddRecipeScreenTabContent(pagerState = pagerState, navController = navController, viewModel = viewModel)

        }
    }
    
}

@Composable
fun RecipeNameField(modifier: Modifier = Modifier, value: String, onNameChange: (String) -> Unit) {

    val focusManager = LocalFocusManager.current

    Surface(modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = notePageColor,
        border = BorderStroke(2.dp, color = MaterialTheme.colors.primary)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = stringResource(id = R.string.write_recipe_recipe_name_label), style = MaterialTheme.typography.h6)
            BasicTextField(
                modifier = Modifier.fillMaxWidth(),
                value = value,
                onValueChange = onNameChange,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                maxLines = 2)
        }
    }

}

@Composable
fun UploadRecipeImageField(modifier: Modifier, onClick: () -> Unit) {

    val stroke = Stroke(pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f), width = 10.dp.value)
    Surface(modifier = modifier.clickable(onClick = onClick)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(color = primary, style = stroke)
            }

            Text(text = stringResource(id = R.string.write_recipe_upload_image_text))

        }
    }
}

@Composable
fun ImageSelectedView(modifier: Modifier, image: Bitmap?, onClick: () -> Unit, viewModel: WriteRecipeScreenViewModel) {

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(text = stringResource(id = R.string.write_recipe_recipe_image_label))
        Spacer(modifier = Modifier.width(10.dp))
        Surface(
            modifier = Modifier
                .size(100.dp, 60.dp)
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colors.primary)) {
            Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                if (image != null) {
                    Image(
                        bitmap = image.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop)
                } else {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AddRecipeScreenTabContent(pagerState: PagerState, navController: NavController, viewModel: WriteRecipeScreenViewModel) {
    HorizontalPager(count = TABS, state = pagerState) { page ->
        when (page) {
            INGREDIENTS_TAB -> IngredientsTab(viewModel = viewModel)
            PROCESS_TAB -> ProcessTab(navController = navController, viewModel = viewModel)
        }
    }
}
