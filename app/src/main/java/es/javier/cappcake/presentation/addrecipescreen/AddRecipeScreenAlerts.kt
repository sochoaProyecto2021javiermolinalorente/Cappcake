package es.javier.cappcake.presentation.addrecipescreen

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import es.javier.cappcake.R

@Composable
fun InvalidRecipeAlert(showDialog: MutableState<Boolean>) {
    AlertDialog(
        title = {
            Text(text = stringResource(id = R.string.add_recipe_recipe_not_valid_alert_title))
        },
        text = {
            Text(text = stringResource(id = R.string.add_recipe_recipe_not_valid_alert_text))
        },
        confirmButton = {
            TextButton(onClick = { showDialog.value = false }) {
                Text(text = stringResource(id = R.string.add_recipe_recipe_not_valid_alert_confirm_button).uppercase())
            }
        },
        onDismissRequest = { },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    )
}