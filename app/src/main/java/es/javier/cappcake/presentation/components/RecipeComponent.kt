package es.javier.cappcake.presentation.components

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.javier.cappcake.R
import es.javier.cappcake.presentation.ui.theme.CappcakeTheme

@Composable
fun RecipeComponent(
    modifier: Modifier = Modifier,
    userName: String,
    title: String,
    recipeImage: Bitmap? = null,
    onClick: () -> Unit
) {

    Surface(
        shape = RectangleShape,
        elevation = 5.dp,
        border = BorderStroke(width = 0.dp, color = Color.Gray),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ProfileImage(modifier = Modifier.size(50.dp).padding(10.dp), onClick = {})
                    Text(text = userName, style = MaterialTheme.typography.body1)
                }
                Divider(color = Color.Black)
            }

            recipeImage?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = modifier.fillMaxWidth()
                )
            }

            Image(
                painter = painterResource(id = R.drawable.burgir),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Divider(thickness = 0.dp, color = Color.Black)

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(10.dp))
            }

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RecipeComponentPreview() {
    CappcakeTheme {
        RecipeComponent(userName = "User_cooker123", title = "Hamburguesa con queso") {}
    }
}