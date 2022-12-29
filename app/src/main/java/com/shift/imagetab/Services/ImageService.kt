package com.shift.imagetab.Services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.shift.imagetab.InjectModules.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface IImageService{
    public fun GetImage()
}

class ImageService @Inject constructor(@ApplicationContext val context: Context,
                                       @IoDispatcher val ioDispatcher: CoroutineDispatcher

) {

        val loader = context.imageLoader



    suspend fun GetImage() : Bitmap {
        return withContext(ioDispatcher){
            val request = ImageRequest.Builder(context)
                .data("https://picsum.photos/200/400")
                .allowHardware(false) // Disable hardware bitmaps.
                .build()

            val result = (loader.execute(request) as SuccessResult).drawable
            val bitmap = (result as BitmapDrawable).bitmap
            bitmap
        }
    }
    fun GetImageRequest(){

    }
}