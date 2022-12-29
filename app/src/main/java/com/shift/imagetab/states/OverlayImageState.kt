package com.shift.imagetab.states

import androidx.compose.runtime.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.mxalbert.zoomable.ZoomableState
import com.mxalbert.zoomable.rememberZoomableState

class OverlayImageState @OptIn(ExperimentalPagerApi::class) constructor(
    val pagerState: PagerState = PagerState(),
    initialVisibility : Boolean = false
) {
    var popinBarsVisible by mutableStateOf(initialVisibility)
    var selectedIndex = 0
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun rememberOverlayImageState(pagerState: PagerState = rememberPagerState(),
                              zoomableState: ZoomableState = rememberZoomableState(minScale = 1f, maxScale = 4f)
) = remember{ OverlayImageState(pagerState = pagerState)}


