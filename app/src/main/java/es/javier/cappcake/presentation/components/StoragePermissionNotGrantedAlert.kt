package es.javier.cappcake.presentation.components

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
import es.javier.cappcake.R

@Composable
fun StoragePermissionNotGrantedAlert(showAlert: MutableState<Boolean>) {

    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { showAlert.value = false },
        title = {
            Text(text = stringResource(id = R.string.write_recipe_storage_alert_title))
        },
        text = {
            Text(text = stringResource(id = R.string.write_recipe_storage_alert_text))
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
                Text(text = stringResource(id = R.string.write_recipe_storage_alert_confirm_button).uppercase())
            }
        },
        dismissButton = {
            TextButton(onClick = { showAlert.value = false }) {
                Text(text = stringResource(id = R.string.write_recipe_storage_alert_dismiss_button).uppercase())
            }
        }
    )
}