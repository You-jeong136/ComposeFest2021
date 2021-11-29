package com.example.compose.rally

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.compose.rally.ui.components.RallyTopAppBar
import org.junit.Rule
import org.junit.Test
import java.util.*

class TopAppBarTest {

    //composeTestRule - createAndroidComposeRule : 테스트 중인 compose 콘텐츠를 설정, 상호작용 가능.
    //테스트에서 사용할 컴포즈 ui 선택 -> 이후 setContent 메소드를 이용해 수행. 어디서나 호출 가능 1회 한정
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun rallyTopAppBarTest() {
        val allScreens = RallyScreen.values().toList()

        //setContent 내에서 구현해둔 컴포즈 메소드 호출. 매개변수 전달
        composeTestRule.setContent {
            RallyTopAppBar(
                allScreens = allScreens,
                onTabSelected = { },
                currentScreen = RallyScreen.Accounts
            )
        }
        Thread.sleep(5000)

    }

    @Test
    fun rallyTopAppBarTest_currentTabSelected() {
        val allScreens = RallyScreen.values().toList()
        composeTestRule.setContent {
            RallyTopAppBar(
                allScreens = allScreens,
                onTabSelected = { },
                currentScreen = RallyScreen.Accounts
            )
        }

        //ui 요소 찾기, 속성 확인 및 작성 수행 _ 패턴에 따라 테스트 규칙을 통해 수행
        // 패턴 : composeTestRule{.finder}{.assertion}{.action}
        // compose 테스트 치트 시트 : https://developer.android.com/reference/kotlin/androidx/compose/ui/test/package-summary?hl=ko
        //파인터 : 요소를 하나 이상 선택, assertion 만들거나 작업 실행 가능
        //어설션 : 요소가 있는지, 특정 속성 보유했는지 확인
        //작업 : 클릭, 기타 동작 등 시뮬레이션된 사용자 이벤트 요소에 삽입

        //onNodeWithContentDescription _ 일반적 검색에 편의 파인터
        composeTestRule
            .onNodeWithContentDescription(RallyScreen.Accounts.name)
            .assertIsSelected()
    }

    //탭 레이블이 대문자로 표시되는지 확인 테스트 < 디버깅 학습
    //시멘틱 트리 : 컴포즈는 이 구조를 이용해 화면에서 요소 찾고 속성 읽음.
    @Test
    fun rallyTopAppBarTest_currentLabelExists() {
        val allScreens = RallyScreen.values().toList()
        composeTestRule.setContent {
            RallyTopAppBar(
                allScreens = allScreens,
                onTabSelected = { },
                currentScreen = RallyScreen.Accounts
            )
        }

        //printToLog : 시멘틱 트리 출력
        /*
        *  [SelectableGroup]
           |-Node #4 at (l=44.0, t=110.0, r=110.0, b=176.0)px
           | Role = 'Tab'
           | Selected = 'false'
           | ContentDescription = '[Overview]'
           | Actions = [OnClick]
           | MergeDescendants = 'true'
           | ClearAndSetSemantics = 'true'
           |-Node #7 at (l=198.0, t=110.0, r=495.0, b=176.0)px
        * */
        composeTestRule.onRoot().printToLog("currentLabelExists")

        //시멘틱 트리 로그 출력 시 onNodeWithText에서 찾는 text 속성의 accounts가 없는 것을 확인 할 수 있음. < 오류 이유
        //대신 ContentDescription에 Accounts로 속성이 설정되어 있음을 알 수 있음.
        //onNodeWithText를 onNodeWithContentDescription으로 교체 - 속성 찾음 : 테스트 통과과
       composeTestRule
            //.onNodeWithText(RallyScreen.Accounts.name.uppercase(Locale.getDefault()))
           .onNodeWithContentDescription(RallyScreen.Accounts.name)
           .assertExists()
    }

    @Test
    fun rallyTopAppBarTest_currentLabelExists2() {
        val allScreens = RallyScreen.values().toList()
        composeTestRule.setContent {
            RallyTopAppBar(
                allScreens = allScreens,
                onTabSelected = { },
                currentScreen = RallyScreen.Accounts
            )
        }

        /*
       *  [SelectableGroup]
          |-Node #4 at (l=44.0, t=110.0, r=110.0, b=176.0)px
          | Role = 'Tab'
          | Selected = 'false'
          | ContentDescription = '[Overview]'
          | Actions = [OnClick]
          | MergeDescendants = 'true' // 노드에 하위 항목이 있으나 병합되었음을 알림.
          | ClearAndSetSemantics = 'true'
          |-Node #7 at (l=198.0, t=110.0, r=495.0, b=176.0)px
       * */
        // 본론 _ text 속성이 표시되는지 여부 확인을 위해, 병합되지 않은 트리 출력.
        // useUnmergedTree = true를 onRoot라는 finder에 전달하여 출력 가능.
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentLabelExists")

       /* [SelectableGroup]
        MergeDescendants = 'true'
        |-Node #3 at (l=42.0, t=105.0, r=105.0, b=168.0)px
        | Role = 'Tab'
        | Selected = 'false'
        | StateDescription = 'Not selected'
        | ContentDescription = 'Overview'
        | Actions = [OnClick]
        | MergeDescendants = 'true'
        | ClearAndSetSemantics = 'true'
        |-Node #6 at (l=189.0, t=105.0, r=468.0, b=168.0)px
        | Role = 'Tab'
        | Selected = 'true'
        | StateDescription = 'Selected'
        | ContentDescription = 'Accounts'
        | Actions = [OnClick]
        | MergeDescendants = 'true'
        | ClearAndSetSemantics = 'true'
        |  |-Node #9 at (l=284.0, t=105.0, r=468.0, b=154.0)px
        |    Text = 'ACCOUNTS' <<<< 찾음
        |    Actions = [GetTextLayoutResult]
        |-Node #11 at (l=552.0, t=105.0, r=615.0, b=168.0)px*/

        //text 설정에서 찾는 본 목적으로 동작하기 위해, "ACCOUNTS"라는 텍스트가 있는 노드를 찾는 matcher 작성.
        //해당 노드는 content description의 "Accounts"를 가진 상위 노드임.

        composeTestRule
            .onNode(
                hasText(RallyScreen.Accounts.name.uppercase(Locale.getDefault())) and
                        hasParent(
                            hasContentDescription(RallyScreen.Accounts.name)
                        ),
                useUnmergedTree = true
            )
            .assertExists()

    }

    //codelab 7
    @Test
    fun rallyTopAppBarTest_clickTabs(){
        var currentScreen:RallyScreen = RallyScreen.Overview // 현재 상태
        //컴포즈 테스트 룰에 RallyApp 설정하기
        composeTestRule.setContent {
            RallyApp(currentScreen){ screen-> currentScreen = screen }
        }

        // 모든 탭을 순회하면서 클릭 하고 현재 상태를 확인한다.
        RallyScreen.values().forEach { screen->
            composeTestRule
                .onNodeWithContentDescription(screen.name)
                .performClick()
            assert(currentScreen == screen)
        }
    }

}