package com.shift.imagetab.ViewModels

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.shift.imagetab.Services.ImageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@HiltViewModel
class StartViewModel @Inject constructor(
    val imageService: ImageService
) : ViewModel() {

    suspend fun GetImage() : Bitmap {
        return imageService.GetImage()
    }

    fun getImageRequest(index:Int) : MutableState<Bitmap?>{
        Log.d("ImageRequest","start request")
        val bitmap = mutableStateOf<Bitmap?>(null);
        viewModelScope.launch {
            val bitmapRequest = imageService.GetImage()
            bitmap.value = bitmapRequest
            Log.d("ImageRequest","Finish Request")
        }
        return bitmap;
    }

}

class Status(val bitmap: Bitmap? = null){
    companion object{
        public val SUCCESS = Status()
    }
}