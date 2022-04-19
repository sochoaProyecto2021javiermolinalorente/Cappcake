package es.javier.cappcake.presentation.registerscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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

            ProfileImage(modifier = Modifier
                .padding(30.dp))

            Surface(
                modifier = Modifier
                    .padding(top = 10.dp, start = 20.dp, end = 20.dp)
                    .fillMaxSize(),
                shape = RoundedCornerShape(36.dp).copy(
                    bottomEnd = ZeroCornerSize,
                    bottomStart = ZeroCornerSize
                ), elevation = 4.dp
            ) {


                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    /*Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                        Button(
                            onClick = { /* TODO register business logic */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .padding(horizontal = 80.dp),
                            shape = RoundedCornerShape(8.dp),
                            enabled = true
                        ) {
                            Text(text = stringResource(id = R.string.login_button_text).uppercase())
                        }
                    }*/
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
                .clickable(enabled = true, role = Role.Image, onClick = onClick)
                .border(width = 5.dp, color = orangish, shape = CircleShape)) {

            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "empty profile image")
        }
        Box(modifier = Modifier.align(alignment = Alignment.BottomEnd).size(30.dp)) {
            Icon(imageVector = Icons.Filled.AddCircle, contentDescription = "", tint = Color.Gray, modifier = Modifier.fillMaxSize())
        }
    }
}

@Preview(name = "Register screen", showSystemUi = true, showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(navController = NavHostController(LocalContext.current), viewModel = viewModel())
}