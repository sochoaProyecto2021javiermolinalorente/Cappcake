package es.javier.cappcake.data.data_sources

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import es.javier.cappcake.domain.Response
import javax.inject.Inject

class LoadImage @Inject constructor(@ApplicationContext private val context: Context) {

    /**
     * Method to load an image from url path
     *
     * @param url THe url path of the image
     * @return The response with the bitmap of the image
     */
    suspend fun loadImage(url: String) : Response<Bitmap?> {

        val imageLoader = ImageLoader.Builder(context)
            .build()

        val imageRequest = ImageRequest.Builder(context)
            .data(url)
            .build()

        return try {
            val bitmap = imageLoader.execute(imageRequest).drawable?.toBitmap()
            if (bitmap != null) {
                Response.Success(data = bitmap)
            } else {
                Response.Failiure(data = null, throwable = null)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Response.Failiure(data = null, throwable = ex)
        }

    }

}