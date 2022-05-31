package es.javier.cappcake.presentation.searchscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import es.javier.cappcake.Navigation
import es.javier.cappcake.R
import es.javier.cappcake.domain.user.User
import es.javier.cappcake.presentation.components.ProfileImage
import kotlinx.coroutines.launch

@Composable
fun SearchUserScreen(navController: NavController, viewModel: SearchUserScreenViewModel) {

    val coroutineScope = rememberCoroutineScope()

    Column {

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
            }

            SearchUserBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 20.dp, end = 20.dp),
                value = viewModel.searchText,
                onValueChange = { viewModel.searchText = it },
                onSearchClick = {
                    coroutineScope.launch { viewModel.searchUsers() }
                }
            )
        }

        if (viewModel.searching) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn {
                items(viewModel.users, key = { it.userId }) {
                    UserSearchItem(modifier = Modifier.fillMaxWidth(), user = it) {
                        navController.navigate(Navigation.ProfileScreen.navigationRoute + "?userId=${it.userId}")
                    }
                }
            }
        }
    }

}

@Composable
fun SearchUserBar(modifier: Modifier,
                  value: String,
                  onValueChange: (String) -> Unit,
                  onSearchClick: () -> Unit) {

    val focusManager = LocalFocusManager.current

    Surface(
        modifier = modifier,
        elevation = 4.dp,
        shape = CircleShape
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.padding(5.dp)
            )

            Box {
                BasicTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = value,
                    onValueChange = onValueChange,
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            onSearchClick()
                        }
                    )
                )

                if (value.isBlank()) {
                    Text(text = stringResource(id = R.string.search_search_hint), color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun UserSearchItem(modifier: Modifier, user: User, onUserClick: () -> Unit) {

    Row(
        modifier = modifier.clickable(onClick = onUserClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileImage(
            modifier = Modifier
                .size(50.dp)
                .padding(5.dp),
            imagePath = user.profileImage)
        Text(
            text = user.username ?: "Username",
            style = MaterialTheme.typography.body1,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }

}
