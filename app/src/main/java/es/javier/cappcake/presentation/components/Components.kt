package es.javier.cappcake.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import es.javier.cappcake.R

@Composable
fun EmailOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = false,
        label = { Text(text = stringResource(id = R.string.login_email_hint)) },
        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
        singleLine = true,
        modifier = modifier.onFocusChanged { focusState ->
            isFocused = focusState.isFocused
        },
        colors = if (!isFocused) TextFieldDefaults.outlinedTextFieldColors() else {
            TextFieldDefaults.outlinedTextFieldColors(
                focusedLabelColor = MaterialTheme.colors.primary,
                leadingIconColor = MaterialTheme.colors.primary
            )
        }
    )
}

@Composable
fun ErrorDialog(showDialog: MutableState<Boolean>,
                @StringRes title: Int,
                @StringRes text: Int) {
    AlertDialog(title = {
        Text(text = stringResource(id = title))
    }, text = {
        Text(text = stringResource(id = text))
    }, buttons = {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            TextButton(onClick = { showDialog.value = false }) {
                Text(text = stringResource(id = R.string.eror_dialog_accept_button))
            }
        }
    }, onDismissRequest = { showDialog.value = false })
}
