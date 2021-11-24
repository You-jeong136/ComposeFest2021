package com.example.compose.rally

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.compose.rally.ui.overview.OverviewBody
import org.junit.Rule
import org.junit.Test

class OverviewScreenTest {

    //테스트 동기화 _ 테스트는 테스트 대상과 동기화가 필요함.
    //ex ) finder 사용 시 테스트는 시맨틱 트리 쿼리 전 앱이 유휴 상태(idle)가 되길 기다림.
    // 이처럼 동기화가 없으면 표시 전 요소 찾는 등 불필요하게 기다리게 됨.
    // _ 기다리는 시간이 길어지게 되면 timeout으로 테스트 실패함. _ 컴포즈가 무언가를 계속하는 등(busy) (무한 애니메이션)

    //overViewScreen에서 body의 alertCard는 반짝이는 무한 애니메이션 -> 수정 필요
    //그래도 무한 애니메이션은 테스트가 이해하는 특이 케이스로, 테스트 busy 유지x
    // 적절 api로 무한 애니메이션 부분을 수정해주면 테스트 가능.

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun overviewScreen_alertsDisplayed() {
        composeTestRule.setContent {
            OverviewBody()
        }

        composeTestRule
            .onNodeWithText("Alerts")
            .assertIsDisplayed()
    }
}