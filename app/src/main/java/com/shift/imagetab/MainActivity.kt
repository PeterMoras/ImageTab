package com.shift.imagetab

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.shift.imagetab.navigation.Navigation
import com.shift.imagetab.ui.theme.ImageTabTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import java.util.*

@HiltAndroidApp
class ImageTabApp : Application(){
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window,false)

        // Remember a SystemUiController
       // SystemUI
        //val systemUiController = com.google.accompanist.

        setContent {
            ImageTabTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = !isSystemInDarkTheme()

                DisposableEffect(systemUiController, useDarkIcons) {
                    // Update all of the system bar colors to be transparent, and use
                    // dark icons if we're in light theme
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = useDarkIcons
                    )

                    // setStatusBarColor() and setNavigationBarColor() also exist

                    onDispose {}
                }


                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    //Greeting("Android")
                    Navigation()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ImageTabTheme {
        Greeting("Android")
    }
}

@Preview(showBackground = true)
@Composable
fun LazyGrid(){
    val list by remember{ mutableStateOf(listOf("A","E","I","O","U")) };

    ImageTabTheme{
        LazyVerticalGrid(
            GridCells.Fixed(3),

        ){
            items(list){
                ColorBox(text = it)
            }
        }
    }
}
@Composable
fun ColorBox(text:String){
    val color = remember{ randomColor() }
    Box(modifier = Modifier
        .background(color)
        .size(200.dp, 200.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text)
    }
}

fun randomColor() : Color{
    val rnd = Random()
    return Color( rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
}