package com.shift.imagetab.Views

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.versionedparcelable.VersionedParcelize

@Preview
@Composable
fun DataFlipper(){
    val (flip,setFlip) = remember { mutableStateOf(Flipable("Cool guy",true))}
    Column {
        Button(onClick = {
            flip.name = "not cool guy"
            setFlip(flip)
        }){
            Text("Click Me")
        }
        Text(text = flip.name + " " + flip.cool)
    }


}

@VersionedParcelize
data class Flipable(var name :String, var cool : Boolean)