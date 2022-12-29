package com.shift.imagetab.Views

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shift.imagetab.ViewModels.ImageGridViewModel
import com.shift.imagetab.states.rememberImageGridViewerState
import kotlinx.coroutines.launch


@Composable
fun StatefulScaffold(vm : ImageGridViewModel = viewModel()){
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val state = rememberImageGridViewerState()
    val data = remember{ generateLoadableImages(50) }

    BackHandler(scaffoldState.drawerState.isOpen) {
        coroutineScope.launch {
            scaffoldState.drawerState.close()
        }
    }

    Box(){
        Scaffold(modifier = Modifier.fillMaxSize(1f),scaffoldState = scaffoldState,
            drawerContent = {
                SidePanel()
            }, content = { padding ->
                val test = padding
                GridImageViewer(data,state)
            })
    }

}

fun generateLoadableImages(size :Int) : SnapshotStateList<ILoadableImage>{
    val data = mutableStateListOf<ILoadableImage>()
    for(i in 0..size){
        data.add(MyLoadableImage("https://picsum.photos/200/200?v=$i"))
    }
    return data
}

@Preview
@Composable
fun PreviewImageGridScaffold(){
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    BackHandler(scaffoldState.drawerState.isOpen) {
        coroutineScope.launch {
            scaffoldState.drawerState.close()
        }
    }

    Box(){
        Scaffold(modifier = Modifier.fillMaxSize(1f),scaffoldState = scaffoldState,
            drawerContent = {
                SidePanel()
            }, content = { padding ->
                val test = padding
                PreviewGridImageViewer()
            })
    }


}