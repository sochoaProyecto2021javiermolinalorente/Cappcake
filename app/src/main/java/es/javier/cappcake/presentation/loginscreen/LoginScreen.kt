package es.javier.cappcake.presentation.loginscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import es.javier.cappcake.R
import es.javier.cappcake.presentation.ui.theme.*

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginScreenViewModel) {

    Box(modifier = Modifier
        .background(
            brush = Brush.verticalGradient(colors = listOf(primaryVariant, primary))
        )
        .fillMaxSize(), contentAlignment = Alignment.TopCenter) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(80.dp))
            Text(text = stringResource(id = R.string.login_title_text), fontSize = 50.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(50.dp))
            Text(text = stringResource(id = R.string.welcome_text), color = Color.White)

            Surface(modifier = Modifier
                .padding(top = 10.dp, start = 20.dp, end = 20.dp)
                .fillMaxSize(),
                shape = RoundedCornerShape(36.dp).copy(bottomEnd = ZeroCornerSize, bottomStart = ZeroCornerSize), elevation = 4.dp) {


                Column(modifier = Modifier
                    .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    EmailOutlinedTextField(modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp, end = 40.dp, top = 20.dp), value = "", onValueChange = { })
                    Spacer(modifier = Modifier.height(10.dp))
                    PasswordOutlinedTextField(modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp, end = 40.dp), value = "", onValueChange = { })
                    Spacer(modifier = Modifier.height(80.dp))

                    Button(onClick = { /*TODO*/ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .padding(horizontal = 80.dp),
                        shape = RoundedCornerShape(8.dp)) {
                        Text(text = stringResource(id = R.string.login_button_text).uppercase())
                    }
                    Text(text = buildAnnotatedString {
                        append(stringResource(id = R.string.have_no_account_text))
                        withStyle(style = SpanStyle(color = orangish)) {
                            append(" ${stringResource(id = R.string.signup_text)}")
                        }}
                    )
                    Spacer(modifier = Modifier.height(100.dp))
                    Image(
                        painter = painterResource(id = R.drawable.logo_title),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 70.dp))
                }
            }
        }
    }
}

@Composable
fun EmailOutlinedTextField(modifier: Modifier = Modifier, value: String, onValueChange: (String) -> Unit) {
    var isFocused = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = "",
        onValueChange = { },
        enabled = true,
        readOnly = false,
        label = { Text(text = stringResource(id = R.string.email_hint)) },
        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
        singleLine = true,
        modifier = modifier.onFocusChanged { focusState ->
            isFocused.value = focusState.isFocused
        },
        colors = if (!isFocused.value) TextFieldDefaults.outlinedTextFieldColors() else {
            TextFieldDefaults.outlinedTextFieldColors(
                focusedLabelColor = primary,
                leadingIconColor = primary)
        })
}

@Composable
fun PasswordOutlinedTextField(modifier: Modifier = Modifier, value: String, onValueChange: (String) -> Unit) {
    val isFocused = remember { mutableStateOf(false) }
    val showPassword = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = stringResource(id = R.string.password_hint)) },
        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "password") },
        trailingIcon = { IconButton(onClick = { showPassword.value = !showPassword.value }) {
            Icon(if (showPassword.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, contentDescription = "")
        }},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation() ,
        enabled = true,
        readOnly = false,
        singleLine = true,
        modifier = modifier.onFocusChanged { focusState ->
            isFocused.value = focusState.isFocused
        },
        colors = if (!isFocused.value) TextFieldDefaults.outlinedTextFieldColors() else {
            TextFieldDefaults.outlinedTextFieldColors(
                focusedLabelColor = primary,
                leadingIconColor = primary,
                trailingIconColor = primary)
        }

    )
}



@Preview(name = "Login screen preview", showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    CappcakeTheme {
        LoginScreen(navController = NavHostController(context = LocalContext.current), viewModel = viewModel())
    }
}