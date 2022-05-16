package es.javier.cappcake.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import es.javier.cappcake.R
import es.javier.cappcake.presentation.ui.theme.CappcakeTheme

@Composable
fun ProfileImage(modifier: Modifier = Modifier,
                 imagePath: String?,
                 onClick: () -> Unit) {

    Surface(
        modifier = modifier.clickable(enabled = true, onClick = onClick),
        shape = CircleShape,
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colors.primary)
    ) {

        if (imagePath != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imagePath)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop)
        } else {
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = null
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ProfileImagePreview() {
    CappcakeTheme {
        ProfileImage(imagePath = null) {}
    }
}