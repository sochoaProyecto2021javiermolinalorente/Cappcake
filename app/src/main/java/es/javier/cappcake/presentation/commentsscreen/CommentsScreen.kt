package es.javier.cappcake.presentation.commentsscreen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import es.javier.cappcake.R
import es.javier.cappcake.domain.user.User
import es.javier.cappcake.presentation.components.ProfileImage
import es.javier.cappcake.presentation.ui.theme.CappcakeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun CommentsScreen(navController: NavController, viewModel: CommentsScreenViewModel) {

    val coroutineScope = rememberCoroutineScope()

    if (viewModel.showAddCommentAlert.value) {
        AddCommentAlert(
            showAlert = viewModel.showAddCommentAlert
        ) {
            coroutineScope.launch { viewModel.addComment() }
            viewModel.showAddCommentAlert.value = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }

                    Text(text = "Comments",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .weight(1f),
                        color = Color.White)
                }
            }
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

            LazyColumn {

                items(viewModel.comments, key = { it.commentId }) {
                    CommentItem(
                        modifier = Modifier.fillMaxWidth(),
                        comment = it.comment,
                        loadUser = { null })
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
                    text = "¿Qué opinas de esta receta?",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }

        Row {

            Spacer(modifier = Modifier.weight(1f))

            TextButton(onClick = onSendClick) {
                Text(text = "Enviar", color = MaterialTheme.colors.primary)
            }
        }

        Divider()
    }

}

@Composable
fun CommentItem(modifier: Modifier,
                comment: String,
                loadUser: suspend CoroutineScope.() -> User?) {

    var user: User? by remember { mutableStateOf(null) }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        user = loadUser()
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
        }

        Text(
            modifier = Modifier.padding(10.dp),
            text = comment,
            maxLines = if (expanded) Int.MAX_VALUE else 3
        )

        TextButton(onClick = { expanded = !expanded }) {
            Text(
                text = if (expanded) "Read less" else "Read more",
                color = MaterialTheme.colors.primary,
                overflow = TextOverflow.Ellipsis
            )
        }

        Divider()
    }


}

@Composable
fun AddCommentAlert(showAlert: MutableState<Boolean>, onConfirmClick: () -> Unit) {
    AlertDialog(
        onDismissRequest = { showAlert.value = false },
        title = {
            Text(text = "Add comment")
        },
        text = {
            Text(text = "¿Estás seguro de añadir este comentario?")
        },
        confirmButton = {
            TextButton(onClick = onConfirmClick) {
                Text(
                    text = "Aceptar".uppercase(),
                    color = MaterialTheme.colors.primary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { showAlert.value = false }) {
                Text(
                    text = "Cancelar".uppercase(),
                    color = MaterialTheme.colors.primary
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CommentItemPreview() {
    CappcakeTheme {
        CommentItem(
            modifier = Modifier.fillMaxWidth(),
            comment = "Great recipe",
            loadUser = { null })
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CommentScreenPreview() {
    CappcakeTheme {
        CommentsScreen(
            navController = NavController(LocalContext.current),
            viewModel = viewModel())
    }
}

private val comment = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."