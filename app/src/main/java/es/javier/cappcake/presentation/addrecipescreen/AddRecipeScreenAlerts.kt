package es.javier.cappcake.presentation.addrecipescreen

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import es.javier.cappcake.R

@Composable
fun StoragePermissionNotGrantedAlert(showAlert: MutableState<Boolean>) {

    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { showAlert.value = false },
        title = {
            Text(text = stringResource(id = R.string.add_recipe_storage_alert_title))
        },
        text = {
            Text(text = stringResource(id = R.string.add_recipe_storage_alert_text))
        },
        confirmButton = {
            TextButton(onClick = {
                val intent = Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    data = Uri.parse("package:${context.packageName}")
                }
                context.startActivity(intent)
                showAlert.value = false
            }) {
                Text(text = stringResource(id = R.string.add_recipe_storage_alert_confirm_button).uppercase())
            }
        },
        dismissButton = {
            TextButton(onClick = { showAlert.value = false }) {
                Text(text = stringResource(id = R.string.add_recipe_storage_alert_dismiss_button).uppercase())
            }
        }
    )
}

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