package com.codelab.layoutcodelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.codelab.layoutcodelab.ui.theme.LayoutCodeLabTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LayoutCodeLabTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun PhotographerCard(modifier : Modifier = Modifier){
    //modifier 순서 중요 : _ single argument로 연결, 순서에 따라 결과에 영향을 줌.
    // 예를 들어 아래에서 padding을 먼저 줄 경우, 패딩을 포함해 전체를 클릭하는 영역으로 지정할 수 없음.
    // clickable을 먼저해주고, 그 영역에 padding 16dp 을 해야 전체 클릭이 가능.
    Row(
        modifier
            .padding(8.dp)
            .clickable(onClick = {})
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colors.surface)
            .padding(16.dp)

    ){
        Surface (
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
        ){
            //Image here
        }
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)  //텍스트 수직+중앙 정렬.
        ) {
            Text("Alfred Sisley", fontWeight = FontWeight.Bold)
            //contentAlpha _ 콘텐츠 투명도 설정.
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text("3 minutes ago", style = MaterialTheme.typography.body2)
            }
        }
    }


}

@Composable
fun LayoutsCodelab() {
    //scaffold : basic material design 레이아웃 구조를 ui로 구현할 수 있게함.
    // top app bar, bottom app bar, floating action button, drawer , navigation 등 최상위 material 구성에 대한 슬롯 제공함.
    Scaffold (
        topBar = {
            TopAppBar (
                title = {
                    Text(text = "LayoutsCodelab")
                },
                actions = {
                    IconButton(onClick = {}){
                        Icon(Icons.Filled.Favorite, contentDescription = null)
                    }


                }
            )
        }
    ){
        innerPadding ->
        ScrollingList()
        //ImageList()
        //LazyList()
        //BodyContent(Modifier.padding(innerPadding))
    }


}
@Composable
fun BodyContent(modifier: Modifier = Modifier){
    Column( modifier = Modifier){
        Text(text = "Hi there!")
        Text(text = "Thanks for goind through the Layouts codelab")
    }
}

@Composable
fun SimpleList(){
    //scrolling position을 기억, 저장. _ 스크롤 위치나 현재 상태 등
    val scrollState = rememberScrollState()
    Column(Modifier.verticalScroll(scrollState)) {
        repeat(100){
            Text("Item #$it")
        }
    }
}
@Composable
fun LazyList(){
    val scrollState = rememberLazyListState()

    //lazyColum  : recycler view와 동일.
    LazyColumn(state = scrollState){
        items(100){
            Text("Item #$it")
        }
    }
}
@Composable
fun ImageListItem(index : Int){
    Row(verticalAlignment = Alignment.CenterVertically){
        Image(
            painter = rememberImagePainter(
                data = "https://developer.android.com/images/brand/Android_Robot.png"
            ),
            contentDescription = "Android Logo",
            modifier = Modifier.size(50.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text("Item #$index", style = MaterialTheme.typography.subtitle1)
    }
}
@Composable
fun ImageList() {
    val scrollState = rememberLazyListState()
    LazyColumn(state = scrollState){
        items(100){ ImageListItem(it) }
    }

}

@Composable
fun ScrollingList() {
    val listSize = 100
    val scrollState = rememberLazyListState()

    //스크롤 하는 동안, 리스트 렌더링을 블록하는 걸 피하기 위해, 스크롤 APIs를 일시 중단 시킬거임.
    // -> 코루틴 필요. rememberCoroutineScope를 사용해 coroutineScope를 생성.
    val coroutineScope = rememberCoroutineScope()
    Column {
        Row {
            Button(onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(0)
                }
            }) {
                Text("Scoll to the top")
            }
            Button(onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(listSize - 1)
                }
            }) {
                Text("Scoll to the end")
            }
        }

        LazyColumn(state = scrollState) {
            items(listSize) {
                ImageListItem(it)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhotographerCardPreview(){
   LayoutCodeLabTheme {
       PhotographerCard()
   }
}

@Preview
@Composable
fun LayoutsCodelabPreview() {
    LayoutCodeLabTheme {
        LayoutsCodelab()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LayoutCodeLabTheme {
        Greeting("Android")
    }
}