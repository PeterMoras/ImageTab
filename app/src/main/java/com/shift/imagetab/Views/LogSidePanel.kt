package com.shift.imagetab.Views

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animate
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.shift.imagetab.R


@Preview
@Composable
fun PreviewSidePanel(){
    var sideOpen by remember{ mutableStateOf(false) }
    BackHandler(sideOpen) {
        sideOpen = false
    }

    SidePanelScaffold(config = LogSidePanel(), openSidePanel = sideOpen){
        Box(Modifier.background(Color.Red).fillMaxSize(1f).pointerInput(Unit){
            detectTapGestures(onDoubleTap = {
                sideOpen = true
            })
        }){
            
        }
    }
}

@Composable
fun SidePanelScaffold(config : ISidePanel,openSidePanel : Boolean,content : @Composable BoxScope.()->Unit){


    BoxWithConstraints{

        content()
        //if(sideOpen)
        AnimatedVisibility(visible = openSidePanel,
            enter = slideInHorizontally {
                (-constraints.maxWidth)
        }, exit = slideOutHorizontally{
                (-constraints.maxWidth)
        }) {
            SidePanel(config = config)
        }
    }
}


@Composable
fun SidePanel(config : ISidePanel = LogSidePanel()){
    var textState by remember{ config.searchText }

    Column(Modifier.fillMaxSize(1.0f)
    ) {
        TopAppBar(Modifier.weight(1.0f)) {
            Row(){
                TextField(
                    value = textState,
                    onValueChange = { textState = it },
                    keyboardActions = KeyboardActions(onDone = { }),
                    modifier = Modifier.fillMaxWidth(1.0f),
                    singleLine = true
                )
                Button(onClick = { config.search() }) {
                    Image(painter  = painterResource(id = R.drawable.ic_round_search_24), contentDescription = "Search")
                }
            }
        }

        TagViewer(tagListData = config.tagData,
            onClickGo = { name-> config.quickSearch(name) },
            onClickFavorite = { name-> config.toggleFavorite(name)},
            onClickText = { name-> config.toggleSearchTag(name) },
            modifier = Modifier.weight(10f)
            )
        Row(Modifier.fillMaxWidth(1.0f)){
            Button(onClick = { config.clearSearch() }, Modifier.weight(1.0f)){
                Image(painter = painterResource(id = R.drawable.ic_round_remove_circle_outline_24), contentDescription = "clear")
            }
            Button(onClick = { config.search() },Modifier.weight(1.0f)){
                Image(painter = painterResource(id = R.drawable.ic_round_search_24), contentDescription = "Search"
                )
            }
        }
    }

}

@Composable
fun TagViewer(
    tagListData: TagListData,
    onClickText: (name: String) -> Unit,
    onClickFavorite: (name: String) -> Unit,
    onClickGo: (name: String) -> Unit,
    modifier : Modifier = Modifier
){
    Column(modifier = modifier){
        for (tag in tagListData.tags){
            TagView(tag,
                onClickText = onClickText,
                onClickFavorite = onClickFavorite,
                onClickGo = onClickGo
            )
        }
    }

}

@Composable
fun TagView(tagData : TagData, onClickText : (name:String) -> Unit, onClickFavorite : (name:String) -> Unit, onClickGo: (name:String) -> Unit){
    Row(Modifier.border(1.dp,Color.Red)){
        Button(modifier = Modifier.weight(10f), onClick = { onClickText(tagData.name) }, contentPadding = PaddingValues(1.dp)) {
            Text(
                text = tagData.name,
                color = tagData.color
            )
        }
        Button(modifier = Modifier.weight(1f), onClick = { onClickFavorite(tagData.name) },contentPadding = PaddingValues(0.dp)) {
            val favorite = tagData.favorite
            val resource = if(favorite.value) R.drawable.ic_baseline_star_24 else R.drawable.ic_baseline_star_border_24
            Image(painter = painterResource(id = resource), contentDescription = tagData.name)
        }
        Button(modifier = Modifier
            .weight(1f)
            .padding(0.dp), onClick = { onClickGo(tagData.name) },contentPadding = PaddingValues(0.dp)){
            Image(painter = painterResource(id = R.drawable.ic_baseline_arrow_forward_24), contentDescription = "Quick Search",
            contentScale = ContentScale.Crop, )
        }

    }
}

interface ISidePanel{
    val tagData: TagListData
    val searchText: MutableState<String>

    fun search();
    fun toggleSearchTag(name:String);
    fun clearSearch();
    fun quickSearch(name:String);
    fun toggleFavorite(name: String)

}
class LogSidePanel : ISidePanel{
    override val searchText: MutableState<String> = mutableStateOf("")

    override val tagData: TagListData = TagListData(tags = listOf(
            TagData(true,Color.Black,"Yes"),
            TagData(false,Color.Blue,"No"),
            TagData(false,Color.Red,"Maybe")
) )

    val searchTags : MutableList<TagData> = mutableListOf()

    fun generateSearchString(){
        var text = "";
        for(tag in searchTags){
            if(text.isNotEmpty()) text += " "
            text += tag.name;
        }
        searchText.value = text
    }

    override fun search() {
        Log.d("SidePanel","Start Search")
    }

    override fun toggleSearchTag(name: String) {
        Log.d("SidePanel","Toggle Search Tag")
        val tag = tagData.tags.find{ t -> t.name == name } ?: return
        if(searchTags.contains(tag))
            searchTags.remove(tag)
        else
            searchTags.add(tag)
        generateSearchString()
    }

    override fun clearSearch() {
        Log.d("SidePanel","clear Search")
        searchTags.clear()
        searchText.value = ""
    }

    override fun quickSearch(name: String) {
        Log.d("SidePanel","Quick Search")
    }

    override fun toggleFavorite(name: String) {
        val tag = tagData.tags.find{ t -> t.name == name } ?: return
        tag.favorite.value = !tag.favorite.value;

        Log.d("SidePanel","Toggle Favorite ${tag.name} : ${tag.favorite.value}")

    }
}

data class TagListData(val tags : List<TagData>);
data class TagData(
    val fav:Boolean,
    val color : Color,
    val name: String,

    ){
    public val favorite = mutableStateOf(fav)
};