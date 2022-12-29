package com.shift.imagetab.states

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import com.google.accompanist.pager.ExperimentalPagerApi

class ImageGridViewerState(
    initialVisibility: Boolean =false,
    var offset: Offset = Offset(0f,0f),
    var overlayImageState: OverlayImageState
    ) {

    var overlayVisibility by mutableStateOf(initialVisibility)

}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun rememberImageGridViewerState(overlayImageState: OverlayImageState = rememberOverlayImageState()) = remember{ ImageGridViewerState(overlayImageState = overlayImageState) }
