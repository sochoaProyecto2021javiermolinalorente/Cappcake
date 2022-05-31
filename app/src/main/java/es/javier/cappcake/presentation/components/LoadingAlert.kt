package es.javier.cappcake.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun LoadingAlert(title: @Composable (() -> Unit)? = null, text: @Composable (() -> Unit)?) {

    Dialog(onDismissRequest = { }) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.wrapContentSize().background(Color.White,
                shape = RoundedCornerShape(8.dp))) {
            Column(modifier = Modifier.wrapContentSize().padding(20.dp)) {
                title?.let {
                    it()
                    Spacer(modifier = Modifier.height(10.dp))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.width(10.dp))
                    text?.let { it() }
                }
            }
        }
    }
}
