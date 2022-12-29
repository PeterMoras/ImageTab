package com.shift.imagetab.navigation

import android.graphics.Bitmap
import android.media.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.shift.imagetab.ViewModels.ImageGridViewModel
import com.shift.imagetab.ViewModels.StartViewModel
import com.shift.imagetab.Views.PreviewImageGridScaffold
import com.shift.imagetab.Views.StatefulScaffold


@Preview
@Composable
fun Navigation(){
    NavHost(navController = rememberNavController(), startDestination = "Preview"){
        composable("Start"){
            val vm : StartViewModel = hiltViewModel()

            Start(vm);
        }
        composable("Preview"){
            val vm : ImageGridViewModel = hiltViewModel()
            StatefulScaffold(vm)
        }
    }

}

@Composable
fun Start(startViewModel: StartViewModel = viewModel()){
    val total = remember { 10 };
    LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.fillMaxSize(1.0f)){
        items(total){ index->
            val image = remember{ startViewModel.getImageRequest(index) }
            if(image.value == null){
                CircularProgressIndicator()
            }else{
                Box(
                    Modifier
                        .border(2.dp, Color.Red)
                        .fillMaxSize(1.0f)
                        .aspectRatio(1.0f),
                    contentAlignment = Alignment.Center){
                    AsyncImage(model = image.value, contentDescription = "", Modifier.fillMaxSize(1.0f), contentScale = ContentScale.Fit)

                }
                //AsyncImage(model = "https://picsum.photos/200", contentDescription = "ok")
            }
        }
    }
}

