package es.javier.cappcake.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@ViewModelScoped
class ImageCompressor @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        const val MAX_QUALITY = 100
        const val MID_QUALITY = 50
        const val LOW_QUALITY = 10
    }

    suspend fun comporessBitmap(quality: Int, imageUri: Uri) : Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            val imageBitmap = ImageDecoder.decodeBitmap(source)
            val outputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 10, outputStream)
            val byteArray = outputStream.toByteArray()
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        } else {
            val imageBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            val outputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 10, outputStream)
            val byteArray = outputStream.toByteArray()
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }
    }

}