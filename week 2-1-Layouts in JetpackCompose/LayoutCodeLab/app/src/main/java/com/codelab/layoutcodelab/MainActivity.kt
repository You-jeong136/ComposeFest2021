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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
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
        //ScrollingList()
        //ImageList()
        //LazyList()
        BodyContent(Modifier.padding(innerPadding))
    }


}
/*@Composable
fun BodyContent(modifier: Modifier = Modifier){
    Column( modifier = Modifier){
        Text(text = "Hi there!")
        Text(text = "Thanks for goind through the Layouts codelab")
    }
}*/

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

//compose layout 원칙 : compose ui는 다중-pass 측정을 허용하지 않는다.
//레이아웃 요소가 다른 측정 요소들을 시도하기 위해 두번 이상 자식을 측정하지 않는다는 의미.
// 단일 패스 측정이 성능도 좋고, compose가 deep ui tree 효율적 처리 가능

/* //layout modifier : 요소를 측정, 배치하는 것을 수동으로 제어.
//기본 구조
    fun Modifier.customLayoutModifier(...) = Modifier.layout{ measurable, constraints ->
        //measurable : 측정, 배치할 자식요소
        //constraints : 자식의 너비/높이에 대한 최소최대값
    }
*/
/*
fun Modifier.firstBaselineToTop(
    firstBaselineToTop : Dp
) = this.then(
    layout{ measurable, constraints ->
        // 자녀 측정.
        val placeable = measurable.measure(constraints)

        check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
        val firstBaseline = placeable[FirstBaseline]

        val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
        val height = placeable.height + placeableY

        layout(placeable.width, height){
            placeable.placeRelative(0, placeableY)
        }

    }
)
@Preview(showBackground = true)
@Composable
fun TextWithPaddingToBaselinePreview() {
    LayoutCodeLabTheme {
        Text("Hi there!", Modifier.firstBaselineToTop(32.dp))
    }
}
@Preview(showBackground = true)
@Composable
fun TextWithNormalPaddingPreview() {
    LayoutCodeLabTheme  {
        Text("Hi there!", Modifier.padding(top = 32.dp))
    }
}
*/

//layout composable
/*
    //기본 구조 :
@Composable
fun CustomLayout(
    modifier: Modifier = Modifier,
    // custom layout attributes
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // measure and position children given constraints logic here
    }
}
*/
@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    content : @Composable () -> Unit
){
    Layout(
        modifier = modifier,
        content = content
    ) {  measurables, constraints ->

            //각 자녀 측정,
            val placeables = measurables.map { measurable ->
                measurable.measure(constraints)
            }
            var yPosition = 0

            // 레이아웃의 사이즈를 이것이 클 수 있는 만큼으로 지정.
            layout(constraints.maxWidth, constraints.maxHeight) {
                // 부모 레이아웃 안에 자식 배치
                placeables.forEach { placeable ->
                    placeable.placeRelative(x = 0, y = yPosition)
                    yPosition += placeable.height
                }
            }
    }

}

//layouts codelab _ 8
@Composable
fun StaggeredGrid(
    //rows : 그리드 _ 재사용 / 화면에 표시하려는 행 수를 매개변수로 전달.
    modifier: Modifier = Modifier,
    rows: Int = 3,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        //자녀 측정 : 한번만 가능. _ 자녀 각 행의 width / hegith 최대 측정

        //각 행의 너비와 높이의 추적/기록하기. keep track of~
        val rowWidths = IntArray(rows) { 0 }
        val rowHeights = IntArray(rows) { 0 }

        val placeables = measurables.mapIndexed { index, measurable ->
            //각 자식 측정
            val placeable = measurable.measure(constraints)

            //각 행의 너비 기록하고, 각 행의 높이 최대값 기록하기.
            val row = index % rows
            rowWidths[row] += placeable.width
            rowHeights[row] = Math.max(rowHeights[row], placeable.height)

            placeable
        }

        //grid의 너비는 제일 넓은 행
        val width = rowWidths.maxOrNull()
            ?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth

        //grid의 높이는 각 행에서 가장 긴 놈들의 합.
        //높이 제약조건은 강제됨.
        val height = rowHeights.sumOf { it }
            .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        //그리드의 크기를 계산, 각 행의 최대 높이를 앎으로 각 행의 요소를 y 측에 배치할 위치 계산 가능.
        //각 행의 y는 이전 행의 누적 높이를 기준으로 함
        val rowY = IntArray(rows) { 0 }
        for (i in 1 until rows) {
            rowY[i] = rowY[i-1] + rowHeights[i-1]
        }

        //placeable.placeRelative(x, y)를 호출해 자식을 배치
        //이때 여기서는 rowX라는 각행의 x좌표도 추적가능.
        layout(width, height) {
            // x cord we have placed up to, per row
            val rowX = IntArray(rows) { 0 }

            placeables.forEachIndexed { index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width
            }
        }
    }

}

@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp, 16.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            Spacer(Modifier.width(4.dp))
            Text(text = text)
        }
    }
}

val topics = listOf(
    "Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
    "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
    "Religion", "Social sciences", "Technology", "TV", "Writing"
)

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    /*MyOwnColumn(modifier.padding(8.dp)) {
        Text("MyOwnColumn")
        Text("places items")
        Text("vertically.")
        Text("We've done it by hand!")
    }*/

    Row(
        //수정자 순서 _ 강조. : modifier에서 순서 중요.
        //차례대로 처리하게 때문에, padding, size 등의 명령 순서에 따라
        // 결과문이 크게 바뀜.
        //row_horizontalScroll : topic 화면 밖으로 나갈 경우를 대비, 스크롤 가능 항목으로 한번 랩핑하기.
        modifier = modifier
            .background(color = Color.LightGray)
            .padding(16.dp)
            .size(200.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        //행수 rows로 바꿀 수 있음, 없으면 rows = 3
        StaggeredGrid(modifier = modifier, rows = 5) {
            for (topic in topics) {
                Chip(modifier = Modifier.padding(8.dp), text = topic)
            }
        }
    }
}

//layouts _ 코드랩 9 : constraint
@Composable
fun ConstraintLayoutContent(){
    ConstraintLayout {
        //제약 걸 수 있는 composables 참조 만들기
        val (button1, button2, text) = createRefs()

        Button(
            onClick = {/* do something*/},
            modifier = Modifier.constrainAs(button1){
                top.linkTo(parent.top, margin = 16.dp)
            }
        ) {
            Text("Button1")
        }

        Text("Text", Modifier.constrainAs(text){
            top.linkTo(button1.bottom, margin = 16.dp)
            centerAround(button1.end)
        })

        val barrier = createEndBarrier(button1, text)
        Button(
            onClick = {/* do something*/},
            modifier = Modifier.constrainAs(button2){
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(barrier)
            }
        ){
            Text("Button2")
        }
    }
}
@Composable
fun LargeConstraintLayout() {
    ConstraintLayout {
        val text = createRef()

        val guideline = createGuidelineFromStart(fraction = 0.5f)
        Text(
            "This is a very very very very very very very long text",
            Modifier.constrainAs(text) {
                linkTo(start = guideline, end = parent.end)
                width = Dimension.preferredWrapContent
            }
        )
    }
}
@Composable
fun DecoupledConstraintLayout() {
    BoxWithConstraints {
        val constraints = if (maxWidth < maxHeight) {
            decoupledConstraints(margin = 16.dp) // Portrait constraints
        } else {
            decoupledConstraints(margin = 32.dp) // Landscape constraints
        }

        ConstraintLayout(constraints) {
            Button(
                onClick = { /* Do something */ },
                modifier = Modifier.layoutId("button")
            ) {
                Text("Button")
            }

            Text("Text", Modifier.layoutId("text"))
        }
    }
}
//인라인에 제약 걸지 않고, 함수로 분리해주기.
private fun decoupledConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val button = createRefFor("button")
        val text = createRefFor("text")

        constrain(button) {
            top.linkTo(parent.top, margin= margin)
        }
        constrain(text) {
            top.linkTo(button.bottom, margin)
        }
    }
}

//codelab _ 11 : intrinsics (내재)
//compose 자녀 한번만 측정 가능, 측정 전 자녀 정보 필요 시 intrinsics 사용 / 실제 측정 전 자식 쿼리 가능
//ex _ (min/max)intrinsicsWidth / intrinicsHeight : ~ 높이/너비 주어질 경우 콘텐츠를 적절하게 칠할 최소/최대 너비/높이는 얼마?
@Composable
fun TwoTexts(modifier: Modifier = Modifier, text1: String, text2: String) {
    Row(modifier = modifier.height(IntrinsicSize.Min)) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
                .wrapContentWidth(Alignment.Start),
            text = text1
        )

        Divider(color = Color.Black, modifier = Modifier.fillMaxHeight().width(1.dp))
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
                .wrapContentWidth(Alignment.End),
            text = text2
        )
    }
}
@Preview(showBackground = true)
@Composable
fun TwoTextsPreview() {
    LayoutCodeLabTheme  {
        Surface {
            TwoTexts(text1 = "Hi", text2 = "there")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ConstraintLayoutContentPreview() {
    LayoutCodeLabTheme {
        DecoupledConstraintLayout()
    }
}
@Preview(showBackground = true)
@Composable
fun PhotographerCardPreview(){
   LayoutCodeLabTheme {
       PhotographerCard()
   }
}

@Preview(showBackground = true)
@Composable
fun LayoutsCodelabPreview() {
    LayoutCodeLabTheme {
        BodyContent()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LayoutCodeLabTheme {
        Greeting("Android")
    }
}

@Preview(showBackground = true)
@Composable
fun ChipPreview(){
    LayoutCodeLabTheme {
        Chip(text = "Hi there")
    }
}