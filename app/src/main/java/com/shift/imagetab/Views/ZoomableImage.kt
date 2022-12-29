package com.shift.imagetab.Views

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import coil.compose.AsyncImage
import com.mxalbert.zoomable.Zoomable
import com.mxalbert.zoomable.rememberZoomableState

@Composable
fun PreviewZoomableImage(modifier: Modifier = Modifier){
    ZoomableImage(model = "https://picsum.photos/200/200",modifier)
}

@Composable
fun PreviewImage(modifier: Modifier = Modifier){
    AsyncImage(model = "https://picsum.photos/200/200", contentDescription = "", modifier = modifier)
}

@Composable
fun ZoomableImage(model : Any, modifier : Modifier = Modifier){
    val zoomState = rememberZoomableState(
        minScale = 1f,
        maxScale = 4f
    )
    Zoomable(state = zoomState) {
        AsyncImage(model = model, contentDescription = "", modifier = modifier)
    }
}
//@Composable
//fun ZoomableImage(model : Any, modifier: Modifier = Modifier){
//    var zoom by remember { mutableStateOf(1f) }
//    var offset by remember { mutableStateOf(Offset.Zero) }
//    var angle by remember { mutableStateOf(0f) }
//
//    val imageModifier = modifier
//        .fillMaxSize()
//        .pointerInput(Unit) {
//            detectTransformGestures(
//                onGesture = { gestureCentroid, gesturePan, gestureZoom, gestureRotate ->
//                    val oldScale = zoom
//                    val newScale = zoom * gestureZoom
//
//                    offset = (offset + gestureCentroid / oldScale)
//                    zoom = newScale.coerceIn(0.5f..5f)
//                    //angle += gestureRotate
//
//
//                }
//            )
//        }
//        .graphicsLayer {
//            translationX = -offset.x * zoom
//            translationY = -offset.y * zoom
//            scaleX = zoom
//            scaleY = zoom
//            rotationZ = angle
//            TransformOrigin(0f, 0f).also { transformOrigin = it }
//        }
//        .clipToBounds()
//
//    AsyncImage(model = model, contentDescription = "Zoomable Image", modifier = imageModifier)
//}