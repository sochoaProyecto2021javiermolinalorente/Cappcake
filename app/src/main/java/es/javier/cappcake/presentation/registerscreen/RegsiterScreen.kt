package es.javier.cappcake.presentation.registerscreen

import android.Manifest
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
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import es.javier.cappcake.R
import es.javier.cappcake.presentation.ui.theme.orangish
import es.javier.cappcake.presentation.ui.theme.primary
import es.javier.cappcake.presentation.ui.theme.primaryVariant
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import es.javier.cappcake.Navigation
import es.javier.cappcake.presentation.components.EmailOutlinedTextField
import es.javier.cappcake.presentation.components.ErrorDialog
import es.javier.cappcake.presentation.components.StoragePermissionNotGrantedAlert
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterScreenViewModel) {

    val loading by  viewModel.creatingUser.collectAsState()
    val coroutine = rememberCoroutineScope()

    val imageSelector = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { imageUri ->
        imageUri?.let {
            viewModel.updateProfileImage(imageUri = it)
        }
    }

    val storagePermission = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            imageSelector.launch("image/*")
        } else {
            viewModel.showStoragePermissionAlert.value = true
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.creatingUser.collect {
            viewModel.setScreenState()
        }
    }

    if (viewModel.showUserNotCreatedAlert.value) {
        ErrorDialog(
            showDialog = viewModel.showUserNotCreatedAlert,
            title = R.string.user_not_created_title_dialog,
            text = R.string.user_not_created_message_dialog
        )
    }

    if (viewModel.showStoragePermissionAlert.value) {
        StoragePermissionNotGrantedAlert(showAlert = viewModel.showStoragePermissionAlert)
    }

    Box(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(colors = listOf(primaryVariant, primary))
            )
            .fillMaxSize(), contentAlignment = Alignment.TopCenter
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Box(contentAlignment = Alignment.TopCenter, modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .clickable {
                            navController.popBackStack()
                        })
                RegisterAddProfileImage(modifier = Modifier
                    .padding(vertical = 20.dp), profileImage = viewModel.profileImage) { storagePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE) }
            }

            Surface(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxSize(),
                shape = RoundedCornerShape(36.dp).copy(
                    bottomEnd = ZeroCornerSize,
                    bottomStart = ZeroCornerSize
                ), elevation = 4.dp
            ) {


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    UsernameTextField(
                        value = viewModel.usernameField,
                        error = viewModel.usernameExistsError,
                        onValueChange = {
                            viewModel.usernameField = it
                            viewModel.checkAllFieldsAreFilled() },
                        modifier = Modifier.fillMaxWidth(), enabled = viewModel.userFieldEnabled)

                    EmailOutlinedTextField(
                        value = viewModel.emailField,
                        onValueChange = {
                            viewModel.emailField = it
                            viewModel.checkAllFieldsAreFilled() },
                        modifier = Modifier.fillMaxWidth(), enabled = viewModel.emailFieldEnabled
                    )

                    RegisterPasswordOutlinedTextField(value = viewModel.passwordField,
                        onValueChange = {
                            viewModel.passwordField = it
                            viewModel.checkAllFieldsAreFilled() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = viewModel.passwordFieldEnabled,
                        error = viewModel.notEqualPasswordError)

                    RepeatPasswordOutlinedTextField(
                        value = viewModel.repeatPasswordField,
                        onValueChange = {
                            viewModel.repeatPasswordField = it
                            viewModel.checkAllFieldsAreFilled() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = viewModel.repeatPasswordFieldEnabled,
                        error = viewModel.notEqualPasswordError)

                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Button(
                            onClick = { coroutine.launch {
                                viewModel.checkPasswordsAreEqual()
                                if (viewModel.createUser()) {
                                    navController.navigate(Navigation.LoadingScreen.navigationRoute)
                                } else {
                                    viewModel.showUserNotCreatedAlert.value = true
                                }
                            } },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .padding(horizontal = 20.dp),
                            shape = RoundedCornerShape(8.dp),
                            enabled = viewModel.createUserButtonEnabled
                        ) {
                            Text(text = stringResource(id = R.string.register_create_account_text_button).uppercase())
                        }
                        if (loading) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RegisterAddProfileImage(modifier: Modifier = Modifier, profileImage: Bitmap?, onClick: () -> Unit) {
    Box(modifier = modifier) {
        Surface(shape = CircleShape,
            modifier = Modifier
                .size(100.dp)
                .border(width = 5.dp, color = orangish, shape = CircleShape)
                .clickable(enabled = true, role = Role.Image, onClick = onClick)) {

            if (profileImage != null) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    bitmap = profileImage.asImageBitmap(),
                    contentScale = ContentScale.Crop,
                    contentDescription = null)
            } else {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.profile),
                    contentScale = ContentScale.Crop,
                    contentDescription = null)
            }
        }
        Box(modifier = Modifier
            .align(alignment = Alignment.BottomEnd)
            .size(30.dp)) {
            Icon(imageVector = Icons.Filled.AddCircle, contentDescription = "", tint = Color.Gray, modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
fun UsernameTextField(modifier: Modifier = Modifier, value: String, onValueChange: (String) -> Unit, error: Boolean, enabled: Boolean = true) {

    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column {
        OutlinedTextField(
            modifier = modifier.onFocusChanged { isFocused = it.isFocused },
            value = value,
            onValueChange = onValueChange,
            isError = error,
            enabled = enabled,
            label = { Text(text = stringResource(id = R.string.register_username_hint)) },
            leadingIcon = { Icon(imageVector = Icons.Filled.Person, contentDescription = "") },
            keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(FocusDirection.Down)}),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            singleLine = true,
            maxLines = 1,
            colors = if (!isFocused) TextFieldDefaults.outlinedTextFieldColors() else {
                TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = MaterialTheme.colors.primary,
                    leadingIconColor = MaterialTheme.colors.primary,
                    trailingIconColor = MaterialTheme.colors.primary
                )
            }
        )

        if (error) {
            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = stringResource(id = R.string.register_screen_username_exists_error_text),
                color = MaterialTheme.colors.error,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun RegisterPasswordOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    error: Boolean = false
) {
    var isFocused by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = stringResource(id = R.string.register_password_hint)) },
        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "password") },
        trailingIcon = {
            IconButton(onClick = { showPassword = !showPassword }) {
                Icon(
                    if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = ""
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        enabled = enabled,
        readOnly = false,
        maxLines = 1,
        singleLine = true,
        modifier = modifier.onFocusChanged { focusState ->
            isFocused = focusState.isFocused
        },
        isError = error,
        colors = if (!isFocused) TextFieldDefaults.outlinedTextFieldColors() else {
            TextFieldDefaults.outlinedTextFieldColors(
                focusedLabelColor = MaterialTheme.colors.primary,
                leadingIconColor = MaterialTheme.colors.primary,
                trailingIconColor = MaterialTheme.colors.primary
            )
        }

    )
}

@Composable
fun RepeatPasswordOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    error: Boolean = false
) {
    var isFocused by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = stringResource(id = R.string.register_repeat_password_hint)) },
        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "password") },
        trailingIcon = {
            IconButton(onClick = { showPassword = !showPassword }) {
                Icon(
                    if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = ""
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        enabled = enabled,
        readOnly = false,
        maxLines = 1,
        singleLine = true,
        isError = error,
        modifier = modifier.onFocusChanged { focusState ->
            isFocused = focusState.isFocused
        },
        colors = if (!isFocused) TextFieldDefaults.outlinedTextFieldColors() else {
            TextFieldDefaults.outlinedTextFieldColors(
                focusedLabelColor = MaterialTheme.colors.primary,
                leadingIconColor = MaterialTheme.colors.primary,
                trailingIconColor = MaterialTheme.colors.primary
            )
        }

    )
}

@Preview(name = "Register screen", showSystemUi = true, showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(navController = NavHostController(LocalContext.current), viewModel = viewModel())
}