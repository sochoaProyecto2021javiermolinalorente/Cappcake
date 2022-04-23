package es.javier.cappcake.presentation.registerscreen

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import es.javier.cappcake.presentation.components.EmailOutlinedTextField

@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterScreenViewModel) {
    Box(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(colors = listOf(primaryVariant, primary))
            )
            .fillMaxSize(), contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier.align(Alignment.TopStart)
                        .clickable {
                            navController.popBackStack()
                        })
                ProfileImage(modifier = Modifier
                    .padding(vertical = 20.dp))
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

                    UsernameTextField(value = "", onValueChange = {}, modifier = Modifier
                        .fillMaxWidth())

                    EmailOutlinedTextField(value = "", onValueChange = {}, modifier = Modifier
                        .fillMaxWidth())

                    RegisterPasswordOutlinedTextField(value = "", onValueChange = {}, modifier = Modifier
                        .fillMaxWidth())

                    RepeatPasswordOutlinedTextField(value = "", onValueChange = {}, modifier = Modifier
                        .fillMaxWidth())

                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Button(
                            onClick = { /* TODO register business logic */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .padding(horizontal = 20.dp),
                            shape = RoundedCornerShape(8.dp),
                            enabled = true
                        ) {
                            Text(text = "crear cuenta".uppercase())
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileImage(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Box(modifier = modifier) {
        Surface(shape = CircleShape,
            modifier = Modifier
                .size(100.dp)
                .border(width = 5.dp, color = orangish, shape = CircleShape)
                .clickable(enabled = true, role = Role.Image, onClick = onClick)) {

            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "empty profile image")
        }
        Box(modifier = Modifier
            .align(alignment = Alignment.BottomEnd)
            .size(30.dp)) {
            Icon(imageVector = Icons.Filled.AddCircle, contentDescription = "", tint = Color.Gray, modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
fun UsernameTextField(modifier: Modifier = Modifier, value: String, onValueChange: (String) -> Unit) {

    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = modifier.onFocusChanged { isFocused = it.isFocused },
        value = value,
        onValueChange = onValueChange, 
        label = { Text(text = stringResource(id = R.string.register_username_hint)) },
        leadingIcon = { Icon(imageVector = Icons.Filled.Email, contentDescription = "") },
        keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(FocusDirection.Down)}),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
        singleLine = true,
        colors = if (isFocused) TextFieldDefaults.outlinedTextFieldColors(
            focusedLabelColor = MaterialTheme.colors.primary,
            leadingIconColor = MaterialTheme.colors.primary
        ) else TextFieldDefaults.outlinedTextFieldColors()
    )
}

@Composable
fun RegisterPasswordOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true
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
        singleLine = true,
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

@Composable
fun RepeatPasswordOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true
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
        singleLine = true,
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