/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.compose.rally

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.compose.rally.ui.components.RallyTopAppBar
import com.example.compose.rally.ui.theme.RallyTheme

/**
 * This Activity recreates part of the Rally Material Study from
 * https://material.io/design/material-studies/rally.html
 */
class RallyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //RallyApp()
            var currentScreen:RallyScreen by rememberSaveable { mutableStateOf(RallyScreen.Overview) }
            RallyApp(currentScreen){
                    screen-> currentScreen = screen
            }
        }
    }
}
/*코드랩 7 : 다른 탭 선택 시 RallyTopAppBar 선택사항이 변경되는지 확인 테스트
힌트 : 테스트 범위는 RallyApp이 소유한, 상태를 포함해야한다.
행동이 아닌 상태를 확인. UI 상태에 대한 assertion 사용하자.
*/
@Composable
fun RallyApp(currentScreen:RallyScreen, onTabSelected: (RallyScreen) -> Unit) {
    RallyTheme {
        val allScreens = RallyScreen.values().toList()

        //currentScreen : stateful임. 이거에 따라 RallyTopAppBar 영향 받음. 테스트하기 쉽게 state hoisting
        // state hoisting : 상태를 끌어올리기 위한 패턴, 컴포저브 함수 내 선언된 상태변수를 해당 컴포저블 함수의 두개의 매개변수로 바꾸는 것.
        // value : T _ 표시할 현재값. onValueChange : (T) -> Unit : T가 새 값일 경우 이를 변경 요청 이벤트
        // var currentScreen by rememberSaveable { mutableStateOf(RallyScreen.Overview) }
        Scaffold(
            topBar = {
                RallyTopAppBar(
                    allScreens = allScreens,
                    //onTabSelected = { screen -> currentScreen = screen },
                    onTabSelected = onTabSelected,
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            Box(Modifier.padding(innerPadding)) {
                currentScreen.content(onScreenChange = onTabSelected)
            }
        }
    }
}
