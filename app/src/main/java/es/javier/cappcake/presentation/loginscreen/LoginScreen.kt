package es.javier.cappcake.presentation.loginscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import es.javier.cappcake.presentation.Navigation

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginScreenViewModel) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Navigate to sign up screen")
            Button(onClick = { navController.navigate(Navigation.RegisterScreen.navigationRoute) }) {
                Text(text = "Navigate")
            }
        }
    }
}

@Preview(name = "Login screen preview", showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = NavHostController(context = LocalContext.current), viewModel = viewModel())
}