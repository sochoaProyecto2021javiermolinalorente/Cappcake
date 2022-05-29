package es.javier.cappcake.presentation.editprofilescreen

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import es.javier.cappcake.R
import es.javier.cappcake.presentation.components.ErrorDialog
import es.javier.cappcake.presentation.components.LoadingAlert
import es.javier.cappcake.presentation.ui.theme.orangish
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(navController: NavController, viewModel: EditProfileScreenViewModel) {

    val screenState by viewModel.screenState.collectAsState()

    LaunchedEffect(key1 = screenState) {
        when (screenState) {
            EditProfileScreenState.LoadBaseDate -> {
                viewModel.loadUser()
                viewModel.loadProfileImage()
            }
            EditProfileScreenState.Updated -> { navController.popBackStack() }
        }
    }

    val coroutineScope = rememberCoroutineScope()

    val imageSelector = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            coroutineScope.launch {
                viewModel.setProfileImage(uri)
            }
        }
    }

    val requestPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            imageSelector.launch("image/*")
        } else {
            viewModel.showImageOptionAlert.value = true
        }
    }

    if (viewModel.showImageOptionAlert.value) {
        ChangeImageAlert(
            showAlert = viewModel.showImageOptionAlert,
            onSelectImageCLic = {
                requestPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                viewModel.showImageOptionAlert.value = false
            },
            onDeleteImageClick = {
                coroutineScope.launch {
                    viewModel.setProfileImage(null)
                    viewModel.showImageOptionAlert.value = false
                }
            })
    }

    if (viewModel.showCanNotUpdateAlert.value) {
        ErrorDialog(
            showDialog = viewModel.showCanNotUpdateAlert,
            title = R.string.can_not_update_user_alert_title,
            text = R.string.can_not_update_user_alert_message
        )
    }

    if (screenState == EditProfileScreenState.Updating) {
        LoadingAlert(
            title = null,
            text = { Text(text = stringResource(id = R.string.edit_profile_screen_updating_text)) }
        )
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

                    Text(text = stringResource(id = R.string.edit_profile_screen_top_bar_title),
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .weight(1f),
                        color = Color.White)
                }
            }
        }
    ) {
        if (screenState == EditProfileScreenState.LoadBaseDate) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = stringResource(id = R.string.edit_profile_screen_change_profile_photo_text).uppercase(),
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(vertical = 10.dp)
                )

                EditImageProfile(
                    modifier = Modifier.size(130.dp),
                    viewModel.profileImage
                ) { viewModel.showImageOptionAlert.value = true }

                Text(
                    text = stringResource(id = R.string.edit_profile_screen_change_username_text).uppercase(),
                    modifier = Modifier.padding(top = 20.dp)
                )

                NewUsernameTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    value = viewModel.username,
                    onValueChange = {
                        coroutineScope.launch { viewModel.setUsername(it) }
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                Box(modifier = Modifier.padding(40.dp), contentAlignment = Alignment.Center) {
                    Button(
                        enabled = screenState == EditProfileScreenState.DataChanged,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        shape = RoundedCornerShape(8.dp),
                        onClick = {
                            coroutineScope.launch { viewModel.updateUserProfile() }
                        }
                    ) {
                        Text(text = stringResource(id = R.string.edit_profile_screen_send_button_text).uppercase())
                    }

                    if (screenState == EditProfileScreenState.Updating) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

}

@Composable
fun EditImageProfile(modifier: Modifier, image: Bitmap?, onImageClick: () -> Unit) {
    Surface(modifier = Modifier
        .clickable(onClick = onImageClick)
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (image != null) {
                Image(
                    modifier = modifier
                        .clip(CircleShape)
                        .border(width = 2.dp, color = orangish, shape = CircleShape),
                    bitmap = image.asImageBitmap(),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )

            } else {
                Image(
                    modifier = modifier
                        .clip(CircleShape)
                        .border(width = 2.dp, color = orangish, shape = CircleShape),
                    painter = painterResource(id = R.drawable.profile),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }

            Icon(
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                imageVector = Icons.Filled.Edit,
                contentDescription = null,
                tint = MaterialTheme.colors.primary
            )
        }
    }
}

@Composable
fun ChangeImageAlert(showAlert: MutableState<Boolean>,
                     onSelectImageCLic: () -> Unit,
                     onDeleteImageClick: () -> Unit) {

    AlertDialog(
        onDismissRequest = { showAlert.value = false },
        title = {
             Text(
                 text = stringResource(id = R.string.change_photo_alert_title),
                 modifier = Modifier.fillMaxWidth(),
                 textAlign = TextAlign.Center
             )
        },
        buttons = {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

                TextButton(
                    onClick = onSelectImageCLic
                ) {
                    Text(text = stringResource(id = R.string.change_photo_alert_change_photo_text).uppercase())
                }

                TextButton(
                    onClick = onDeleteImageClick
                ) {
                    Text(
                        text = stringResource(id = R.string.change_photo_alert_delete_photo_text).uppercase(), 
                        color = Color.Red
                    )
                }

                TextButton(
                    onClick = { showAlert.value = false }
                ) {
                    Text(text = stringResource(id = R.string.change_photo_alert_cancel_text).uppercase())
                }

            }
        }
    )
}

@Composable
fun NewUsernameTextField(modifier: Modifier, value: String, onValueChange: (String) -> Unit) {

    val focusManager = LocalFocusManager.current

    Box(modifier = modifier.padding(horizontal = 5.dp, vertical = 3.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, shape = RoundedCornerShape(8.dp))
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                .border(1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
                .padding(8.dp),
            value = value,
            onValueChange = onValueChange,
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )

        if (value.isBlank()) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = stringResource(id = R.string.edit_profile_screen_username_hint),
                color = Color.Gray,
            )
        }
    }

}
