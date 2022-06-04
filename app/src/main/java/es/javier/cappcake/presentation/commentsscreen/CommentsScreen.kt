package es.javier.cappcake.presentation.commentsscreen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import es.javier.cappcake.R
import es.javier.cappcake.domain.comment.Comment
import es.javier.cappcake.domain.user.User
import es.javier.cappcake.presentation.components.ErrorDialog
import es.javier.cappcake.presentation.components.LoadingAlert
import es.javier.cappcake.presentation.components.ProfileImage
import es.javier.cappcake.utils.OnBottomReached
import es.javier.cappcake.utils.ScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CommentsScreen(navController: NavController, viewModel: CommentsScreenViewModel, recipeId: String, owner: String) {

    LaunchedEffect(key1 = Unit) {
        if (viewModel.screenState is ScreenState.LoadingData) {
            viewModel.getAllCommentsOf(recipeId)
        }
    }

    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    lazyListState.OnBottomReached {
        coroutineScope.launch {
            viewModel.lastCommentId?.let { viewModel.loadMoreComments(recipeId) }
        }
    }

    Dialogs(viewModel = viewModel, recipeId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.comments_screen_topbar_title),
                        color = Color.White)
                },
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

        Column {

            AddCommentItem(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.currentComment,
                onValuechange = { viewModel.currentComment = it }
            ) {
                viewModel.showAddCommentAlert.value = true
            }

            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = viewModel.isRefreshing),
                onRefresh = { coroutineScope.launch { viewModel.getAllCommentsAgain(recipeId) } }
            ) {

                LazyColumn(
                    state = lazyListState
                ) {

                    items(viewModel.comments, key = { it.commentId }) {

                        if (viewModel.currentEditingCommentId == it.commentId) {
                            EditComment(
                                modifier = Modifier.fillMaxWidth(),
                                value = it.comment,
                                onUpdateClick = { coroutineScope.launch { viewModel.updateComment(it, recipeId) } },
                                onCancelClick = { viewModel.currentEditingCommentId = "" })
                        } else {
                            CommentItem(
                                modifier = Modifier.fillMaxWidth(),
                                comment = it,
                                currentUserId = viewModel.getCurrentId(),
                                owner = owner,
                                loadUser = { viewModel.loadUser(it.userId) },
                                onEditClick = { viewModel.currentEditingCommentId = it.commentId },
                                onRemoveClick = {
                                    viewModel.lastFocusCommentId = it.commentId
                                    viewModel.showRemoveCommentAlert.value = true
                                }
                            )
                        }
                    }

                }

            }

        }

    }


}

@Composable
fun AddCommentItem(modifier: Modifier, value: String, onValuechange: (String) -> Unit, onSendClick: () -> Unit) {

    Column(modifier = modifier) {
        Box(modifier = Modifier.padding(top = 20.dp), contentAlignment = Alignment.CenterStart) {
            Column {
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    value = value,
                    onValueChange = onValuechange,
                    maxLines = 4,
                    textStyle = TextStyle(fontSize = 16.sp)
                )

                Divider(modifier = Modifier.padding(horizontal = 10.dp))
            }

            if (value.isBlank()) {
                Text(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    text = stringResource(id = R.string.comments_screen_add_recipe_hint),
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }

        Row {

            Spacer(modifier = Modifier.weight(1f))

            TextButton(onClick = {
                if (value.isNotBlank())
                    onSendClick()
            }) {
                Text(text = stringResource(id = R.string.comments_screen_send_button_text), color = MaterialTheme.colors.primary)
            }
        }

        Divider()
    }

}

@Composable
fun CommentItem(modifier: Modifier,
                comment: Comment,
                owner: String,
                currentUserId: String?,
                loadUser: suspend CoroutineScope.() -> User?,
                onEditClick: () -> Unit,
                onRemoveClick: () -> Unit) {

    var user: User? by remember { mutableStateOf(comment.user) }
    var expanded by remember { mutableStateOf(false) }
    val ownRecipe = owner == currentUserId
    val ownComment = currentUserId == comment.userId

    LaunchedEffect(key1 = Unit) {
        if (user == null) {
            comment.user = loadUser()
            user = comment.user
        }
    }

    Column(modifier = modifier.animateContentSize()) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileImage(
                modifier = Modifier
                    .size(50.dp)
                    .padding(5.dp),
                imagePath = user?.profileImage)
            Text(
                text = user?.username ?: "Username",
                style = MaterialTheme.typography.body1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (ownRecipe || ownComment) {
                Spacer(modifier = Modifier.weight(1f))

                CommentOptionsDropDownMenu(
                    ownComment = ownComment,
                    onEditClick = onEditClick,
                    onRemoveClick = onRemoveClick
                )
            }
        }

        Text(
            modifier = Modifier.padding(10.dp),
            text = comment.comment,
            maxLines = if (expanded) Int.MAX_VALUE else 3
        )

        TextButton(onClick = { expanded = !expanded }) {
            Text(
                text = if (expanded)
                    stringResource(id = R.string.comments_screen_read_more_button_text)
                else
                    stringResource(id = R.string.comments_screen_read_less_button_text),
                color = MaterialTheme.colors.primary,
                overflow = TextOverflow.Ellipsis
            )
        }

        Divider()
    }
}

@Composable
fun EditComment(modifier: Modifier,
                value: String,
                onUpdateClick: (String) -> Unit,
                onCancelClick: () -> Unit) {

    var comment by remember {
        mutableStateOf(value)
    }

    Column(modifier = modifier) {
        Box(modifier = Modifier.padding(top = 20.dp), contentAlignment = Alignment.CenterStart) {
            Column {
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    value = comment,
                    onValueChange = { comment = it },
                    maxLines = 4,
                    textStyle = TextStyle(fontSize = 16.sp)
                )

                Divider(modifier = Modifier.padding(horizontal = 10.dp))
            }

            if (value.isBlank()) {
                Text(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    text = stringResource(id = R.string.comments_screen_edit_comment_hint),
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }

        Row {

            Spacer(modifier = Modifier.weight(1f))

            TextButton(onClick = onCancelClick) {
                Text(text = stringResource(id = R.string.comments_screen_cancel_button_text), color = Color.Red)
            }

            TextButton(onClick = {
                if (value.isNotBlank())
                    onUpdateClick(comment)
            }) {
                Text(text = stringResource(id = R.string.comments_screen_edit_button_text), color = MaterialTheme.colors.primary)
            }
        }

        Divider()
    }
}

@Composable
fun CommentOptionsDropDownMenu(ownComment: Boolean, onEditClick: () -> Unit, onRemoveClick: () -> Unit) {

    var expanded by remember { mutableStateOf(false) }

    Box {

        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (ownComment) {
                DropdownMenuItem(onClick = onEditClick) {
                    Text(text = stringResource(id = R.string.comments_screen_edit_menu_text), color = MaterialTheme.colors.primary)
                }
            }

            DropdownMenuItem(onClick = onRemoveClick) {
                Text(text = stringResource(id = R.string.comments_screen_delete_menu_text), color = Color.Red)
            }
        }
    }

}

@Composable
fun AddCommentAlert(showAlert: MutableState<Boolean>, onConfirmClick: () -> Unit) {
    AlertDialog(
        onDismissRequest = { showAlert.value = false },
        title = {
            Text(text = stringResource(id = R.string.add_comment_title))
        },
        text = {
            Text(text = stringResource(id = R.string.add_comment_text))
        },
        confirmButton = {
            TextButton(onClick = onConfirmClick) {
                Text(
                    text = stringResource(id = R.string.add_comment_accept_button_text).uppercase(),
                    color = MaterialTheme.colors.primary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { showAlert.value = false }) {
                Text(
                    text = stringResource(id = R.string.add_comment_cancel_button_text).uppercase(),
                    color = MaterialTheme.colors.primary,
                )
            }
        }
    )
}

@Composable
fun DeleteCommentAlert(showAlert: MutableState<Boolean>, onConfirmClick: () -> Unit) {
    AlertDialog(
        onDismissRequest = { showAlert.value = false },
        title = {
            Text(text = stringResource(id = R.string.delete_comment_title))
        },
        text = {
            Text(text = stringResource(id = R.string.delete_comment_text))
        },
        confirmButton = {
            TextButton(onClick = onConfirmClick) {
                Text(
                    text = stringResource(id = R.string.delete_comment_accept_button_text).uppercase(),
                    color = MaterialTheme.colors.primary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { showAlert.value = false }) {
                Text(
                    text = stringResource(id = R.string.delete_comment_cancel_button_text).uppercase(),
                    color = MaterialTheme.colors.primary,
                )
            }
        }
    )
}

@Composable
fun Dialogs(viewModel: CommentsScreenViewModel, recipeId: String) {

    val coroutineScope = rememberCoroutineScope()

    if (viewModel.showAddCommentAlert.value) {
        AddCommentAlert(
            showAlert = viewModel.showAddCommentAlert
        ) {
            coroutineScope.launch { viewModel.addComment(recipeId) }
            viewModel.showAddCommentAlert.value = false
        }
    }

    if (viewModel.showRemoveCommentAlert.value) {
        DeleteCommentAlert(showAlert = viewModel.showRemoveCommentAlert) {
            coroutineScope.launch {
                viewModel.deleteComment(recipeId)
                viewModel.showRemoveCommentAlert.value = false
            }
        }
    }

    if (viewModel.showProcessingAlert.value) {
        LoadingAlert(
            text = { Text(text = stringResource(id = R.string.comment_loading_alert_text)) }
        )
    }

    if (viewModel.showAddCommentErrorAlert.value) {
        ErrorDialog(
            showDialog = viewModel.showAddCommentErrorAlert,
            title = R.string.add_comment_error_alert_title,
            text = R.string.add_comment_error_alert_text
        )
    }

    if (viewModel.showDeleteCommentErrorAlert.value) {
        ErrorDialog(
            showDialog = viewModel.showDeleteCommentErrorAlert,
            title = R.string.delete_comment_error_alert_title,
            text = R.string.delete_comment_error_alert_text
        )
    }

    if (viewModel.showEditCommentErrorAlert.value) {
        ErrorDialog(
            showDialog = viewModel.showEditCommentErrorAlert,
            title = R.string.edit_comment_error_alert_title,
            text = R.string.edit_comment_error_alert_text
        )
    }
}
