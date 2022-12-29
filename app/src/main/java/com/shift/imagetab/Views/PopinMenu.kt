package com.shift.imagetab.Views

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Preview
@Composable
fun PreviewPopIn(){
    val visibility = remember { mutableStateOf(false) }

    BackHandler(visibility.value){
        visibility.value = false;
    }

    PopIn(visible = visibility.value, modifier = Modifier
        .fillMaxSize(1f)
        .pointerInput(Unit) {
            detectTapGestures { visibility.value = true }
        }
    ) { padding ->
        PreviewGridImageViewer()
    }
}

@Composable
fun PopIn(visible : Boolean, modifier : Modifier = Modifier, content : @Composable (padding : PaddingValues)->Unit){
    val state = rememberScaffoldState()
    Scaffold(
        modifier = modifier.fillMaxSize(1f),
        scaffoldState = state,
        topBar = {
        AnimatedVisibility(visible = visible, enter = slideInVertically(), exit =  slideOutVertically{
                fullHeight ->  -fullHeight
        }) {
            TopAppBar() {

            }
        }
    },
        content = { padding ->
            val test = padding
            content(padding)
        },
        bottomBar = {
            AnimatedVisibility(visible = visible,
                enter = slideInVertically { fullHeight -> fullHeight  },
                exit = slideOutVertically { fullHeight -> fullHeight }
            ) {
                BottomAppBar() {
                    Text("Hello")
                }
            }
        },
        drawerContent = {
            SidePanel()
        }
    )
}

@Composable
fun PopInBars(visible : Boolean, modifier : Modifier = Modifier, topBar : @Composable ()->Unit, bottomBar : @Composable ()->Unit ,content : @Composable (padding : PaddingValues)->Unit){
    val state = rememberScaffoldState()
    Scaffold(
        modifier = modifier.fillMaxSize(1f),
        scaffoldState = state,
        topBar = {
            AnimatedVisibility(visible = visible, enter = slideInVertically(), exit =  slideOutVertically{
                    fullHeight ->  -fullHeight
            }) {
                topBar()
            }
        },
        content = { padding ->
            val test = padding
            content(padding)
        },
        bottomBar = {
            AnimatedVisibility(visible = visible,
                enter = slideInVertically { fullHeight -> fullHeight  },
                exit = slideOutVertically { fullHeight -> fullHeight }
            ) {
                bottomBar()
            }
        }
    )
}

@Composable
fun PopInBarsPreset(visible : Boolean, modifier : Modifier = Modifier, topBar : @Composable ()->Unit, bottomBar : @Composable ()->Unit ,content : @Composable ()->Unit){
    BoxWithConstraints(Modifier.background(Color.Transparent)){
        val botLoc = maxHeight
        content()
        AnimatedVisibility(visible = visible, enter = slideInVertically(){ -it }, exit =  slideOutVertically{ -it }) {
            TopAppBar(modifier.height(70.dp)) {
                topBar()
            }
        }

        AnimatedVisibility(visible = visible,
            enter = slideInVertically { it  },
            exit = slideOutVertically { it }
        ) {
            BottomAppBar(modifier.height(70.dp).offset(0.dp,maxHeight - 70.dp)) {
                bottomBar()
            }

        }


    }

}


@Preview
@Composable
fun PreviewInsets(){

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
    Box(Modifier.systemBarsPadding()) {
        Box(Modifier.imePadding()) {
            PreviewZoomableImage(Modifier.fillMaxSize(1f))
        }
    }
}





