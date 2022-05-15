package es.javier.cappcake.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import es.javier.cappcake.R

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
                Text(text = stringResource(id = R.string.eror_dialog_accept_button).uppercase())
            }
        }
    }, onDismissRequest = { showDialog.value = false })
}