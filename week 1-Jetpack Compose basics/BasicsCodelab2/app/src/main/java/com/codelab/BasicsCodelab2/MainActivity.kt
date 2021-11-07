package com.codelab.BasicsCodelab2

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codelab.BasicsCodelab2.ui.theme.BasicsCodelab2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* setContent 안에 xml 대신 레이아웃을 구성할 Composable 함수가 들어갈 수 있다. */

        setContent {
            BasicsCodelab2Theme {
                MyApp()
            }
        }
    }
}
@Composable
private fun MyApp(){
    //remeber일 경우, 앱을 끄거나 다크모드 등을 전환 시 처음으로 돌아가 시작
    //remebersaveable 일 경우, 전환 했다 다시 들어가도, 보던 상태가 유지됨.
    var shouldShowOnboarding by rememberSaveable{ mutableStateOf(true) }

    if (shouldShowOnboarding) {
        OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
    } else {
        Greetings()
    }
}
@Composable
private fun Greetings(names: List<String> = List(1000){ "$it" } ) {
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = names){ name ->
            Greeting(name = name)
        }
    }
}

@Composable
fun OnboardingScreen(onContinueClicked: () -> Unit){

    Surface{
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text("Welcome to the Basics Codelab!")
            Button(
                modifier = Modifier.padding(vertical = 24.dp),
                onClick = onContinueClicked
            ){
                Text("Continue")
            }
        }
    }
}

@Composable
private fun Greeting(name : String){

    //expanded _ remebmer일 경우, 화면을 벗어나서 아래까지 스크롤 시 상태가 초기화 _
    //remembersavable이여야 아래까지 스크롤했다 돌아와도, 그 전 상태를 유지함.
    val expanded = rememberSaveable { mutableStateOf(false)}
    val extraPadding by animateDpAsState( if(expanded.value) 48.dp else 0.dp )
    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        //row는 수평으로
        Row(modifier = Modifier.padding(24.dp)) {
            //Column은 수직으로 아이템을 쌓아내려감.
            //전체 넓이 잡는거는 weight를 1로 설정하는 것 외에도 fillMatWidth()가 있음.
            Column(modifier = Modifier
                .weight(1f)
                .padding(bottom = extraPadding)
            ) {
                Text(text = "Hello, ",  style = MaterialTheme.typography.h6)
                Text(text = name,
                        style = MaterialTheme.typography.h4.copy(
                            fontWeight = FontWeight.ExtraBold
                        )
                )
            }
            OutlinedButton(
                onClick = { expanded.value = !expanded.value },
            ) {
                Text(
                    if(expanded.value) "show less" else "show more",
                    color =  MaterialTheme.colors.secondary
                )
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    BasicsCodelab2Theme {
        OnboardingScreen(onContinueClicked = {})
    }
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    widthDp = 320,
    name = "Dark Mode"
)
@Preview(name = "Light Mode", showBackground = true)
@Composable
fun DefaultPreview() {
    BasicsCodelab2Theme {
       Greetings()
    }
}