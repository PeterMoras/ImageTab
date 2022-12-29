package com.shift.imagetab.Views

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.mxalbert.zoomable.Zoomable
import com.mxalbert.zoomable.ZoomableState
import com.mxalbert.zoomable.rememberZoomableState
import com.shift.imagetab.R
import com.shift.imagetab.states.ImageGridViewerState
import com.shift.imagetab.states.OverlayImageState
import com.shift.imagetab.states.rememberImageGridViewerState
import com.shift.imagetab.states.rememberOverlayImageState
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun PreviewGridImageViewer(){
    val data = remember {
        List(50){ i -> LoadableImage(mutableStateOf("https://picsum.photos/200/200?v=$i"))}
    }
    val offset = remember { mutableStateOf(Offset(0f,0f)) }
    var overlay by remember { mutableStateOf(false) }
    var selectedIndex by remember{ mutableStateOf(0) }
    val width = remember { mutableStateOf(1f) }
    val height = remember { mutableStateOf(1f) }
    BackHandler(overlay) {
        overlay = false
    }
    BoxWithConstraints(Modifier.fillMaxSize(1f)){
        ImageGrid(data,data.count(),onImageClick = { index,loc ->
            offset.value = loc
            overlay = true
            selectedIndex = index
        }, modifier = Modifier)

        with(LocalDensity.current){
            width.value = maxWidth.value * this.density
            height.value = maxHeight.value * this.density
        }

        AnimatedVisibility(visible = overlay, enter = scaleIn(
            transformOrigin =  TransformOrigin(offset.value.x/width.value,offset.value.y/height.value),
            initialScale = 0.2f
        )) {

            OverlayImageView(data = data)
        }
        //if(overlay)
//            FullImageView(data[selectedIndex], modifier = Modifier
//                .fillMaxSize(1f)
//                .background(Color(0, 0, 0, 0x88)))
    }
}
@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun GridImageViewer(data : SnapshotStateList<ILoadableImage> , state : ImageGridViewerState = rememberImageGridViewerState()){

    val coroutineScope = rememberCoroutineScope()

    val width = remember { mutableStateOf(1f) }
    val height = remember { mutableStateOf(1f) }
    BackHandler(state.overlayVisibility) {
        state.overlayVisibility = false
    }
    BoxWithConstraints(Modifier.fillMaxSize(1f)){
        ImageGrid(data,data.count(),onImageClick = { index,loc ->
            state.offset = loc
            state.overlayVisibility = true
            state.overlayImageState.selectedIndex = index
//            coroutineScope.launch {
//                state.overlayImageState.pagerState.scrollToPage(index)
//            }
        }, modifier = Modifier)

        with(LocalDensity.current){
            width.value = maxWidth.value * this.density
            height.value = maxHeight.value * this.density
        }

        AnimatedVisibility(visible = state.overlayVisibility, enter = fadeIn() + scaleIn(
            transformOrigin =  TransformOrigin(state.offset.x/width.value,state.offset.y/height.value),
            initialScale = 0.2f
        )) {

            OverlayImageView(data = data, state = state.overlayImageState)
        }

    }
}



@OptIn(ExperimentalPagerApi::class)
@Composable
fun OverlayImageView(data : List<ILoadableImage>, state : OverlayImageState = rememberOverlayImageState(), topBar: @Composable ()->Unit = {}, bottomBar : @Composable ()->Unit = {}){
    BackHandler(state.popinBarsVisible) {
        state.popinBarsVisible = false
    }

    LaunchedEffect(key1 = state.selectedIndex ){
        state.pagerState.scrollToPage(state.selectedIndex)
        //state.scrollToPage(overlayState.selectedIndex)
    }


    PopInBarsPreset(visible = state.popinBarsVisible, modifier = Modifier.background(Color.Transparent),topBar = topBar, bottomBar =  bottomBar) {
        Box{

            HorizontalPager(count = data.count(), state = state.pagerState) { currentPage ->
                val zoomableState = rememberZoomableState()
                FullImageView(
                    image = data[currentPage],
                    zoomableState = zoomableState,
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .background(Color(0, 0, 0, 0xAA)),
                    onTap = { state.popinBarsVisible = !state.popinBarsVisible }
                )
            }
        }

    }
}





@Composable
fun ImageGrid(data : List<ILoadableImage>, itemCount : Int,modifier: Modifier = Modifier, cells : Int = 3, onImageClick : (index :Int, offset:Offset) -> Unit){
    val state = rememberLazyGridState()
    val offset by remember { derivedStateOf { state.firstVisibleItemIndex } }
    Column{
        TopAppBar(Modifier.imePadding()) {
            Text(text = "$offset / $itemCount", modifier = Modifier.fillMaxSize(1f), textAlign = TextAlign.Center)
            //(text = "$offset / $itemCount", modifier = Modifier.fillMaxSize(1f), textAlign = Alignment.Center)
        }
        LazyVerticalGrid(columns = GridCells.Fixed(cells),
            state = state,
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(3.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ){
            items(itemCount){ index ->
                PreviewImage(index, data[index], onClick = onImageClick)
            }
        }
    }
}
@Composable
fun FullImageView(image : ILoadableImage, modifier : Modifier = Modifier, zoomableState: ZoomableState = rememberZoomableState(), onTap : (offset : Offset )->Unit){
    //ZoomableImage(model = image.model.value, modifier = modifier)

    Zoomable(state = zoomableState, onTap = onTap) {
        AsyncImage(model = image.model.value, contentDescription = "", modifier = modifier)
    }
}

@Composable
fun PreviewImage(index :Int, image: ILoadableImage, onClick : (index:Int, offset : Offset) -> Unit){
    val globalPosition = remember { mutableStateOf(Offset(0f,0f)) }
    Box(
        Modifier
            .border(1.dp, Color.Black, shape = RoundedCornerShape(1.dp))
            .onGloballyPositioned { coordinates ->
                globalPosition.value = coordinates.positionInRoot()
            }
            .pointerInput(Unit) {
                detectTapGestures { offset -> onClick(index, globalPosition.value + offset) }
            }, propagateMinConstraints = true){
        AsyncImage(model = image.model.value, contentDescription = "PreviewImage", contentScale = ContentScale.Crop)
        Text(text="20",
            Modifier
                .align(Alignment.TopStart)
                .offset(4.dp, 2.dp))
    }
}



interface ILoadableImage{
    val model : MutableState<Any>
}
data class LoadableImage(override val model: MutableState<Any>) : ILoadableImage{

}
class MyLoadableImage(model:Any)  :ILoadableImage{
    override val model: MutableState<Any> = mutableStateOf(model)
}

interface IGridSource{

}

