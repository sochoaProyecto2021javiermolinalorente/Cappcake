package es.javier.cappcake.presentation.profilescreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun ProfileScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            Text(text = "Profile screen")
            Button(onClick = { signOut(navController = navController) }) {
                Text(text = "Cerrar sesión")
            }
        }
    }
}

private fun signOut(navController: NavController) {
    /*Firebase.auth.currentUser?.delete()
        ?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navController.navigate(navController.graph.findStartDestination().id)
            }
        }*/

    Firebase.auth.signOut()
    navController.navigate(navController.graph.findStartDestination().id)

}