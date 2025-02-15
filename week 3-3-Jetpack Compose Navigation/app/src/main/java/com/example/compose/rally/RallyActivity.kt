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
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.compose.rally.ui.components.RallyTabRow
import com.example.compose.rally.ui.theme.RallyTheme
import com.example.compose.rally.data.UserData
import com.example.compose.rally.ui.accounts.AccountsBody
import com.example.compose.rally.ui.bills.BillsBody
import com.example.compose.rally.ui.overview.OverviewBody
import com.example.compose.rally.RallyScreen.Accounts
import com.example.compose.rally.RallyScreen.Bills
import com.example.compose.rally.RallyScreen.Overview
import com.example.compose.rally.ui.accounts.SingleAccountBody


/**
 * This Activity recreates part of the Rally Material Study from
 * https://material.io/design/material-studies/rally.html
 */
class RallyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RallyApp()
        }
    }
}


@Composable
fun RallyApp() {
    RallyTheme {

        val allScreens = RallyScreen.values().toList()
        // set up navController : nav 생성 & 살아남은 구성 변경을 기억
        val navController = rememberNavController()
        val backstackEntry = navController.currentBackStackEntryAsState()
        //navigation 백 스택 유지 _ 그리고 State로 현재 백스택 엔트리를 제공함.
        //이 state로, 백스택의 변경사항에 반응가능, 경로에 대한 현재 백스택 항목 쿼리도 가능.
        val currentScreen = RallyScreen.fromRoute(
            backstackEntry.value?.destination?.route
        )

        Scaffold(
            topBar = {
                RallyTabRow(
                    allScreens = allScreens,
                    onTabSelected = { screen -> navController.navigate(screen.name) },
                    currentScreen = currentScreen,
                )
            }
        ) { innerPadding ->
            RallyNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
//navhost 추출 -> navController 내부에 만들지 않음으로서 RallyApp 내에서 상위 구조 탭을 가짐
// -> 탭 선택 시 이를 계속 사용 가능.
@Composable
fun RallyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Overview.name,
        modifier = modifier
    ) {

        composable(Overview.name) {
            OverviewBody(
                onClickSeeAllAccounts = { navController.navigate(Accounts.name) },
                onClickSeeAllBills = { navController.navigate(Bills.name) },
                onAccountClick = { name -> navigateToSingleAccount(navController, name) },
            )
        }
        composable(Accounts.name) {
            AccountsBody(accounts = UserData.accounts) { name ->
                navigateToSingleAccount(
                    navController = navController,
                    accountName = name
                )
            }
        }
        composable(Bills.name) {
            BillsBody(bills = UserData.bills)
        }

        val accountsName = Accounts.name

        composable(
            "$accountsName/{name}",
            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                }
            ),
            //navDeepLink 이용 _ deepLinks 목록 추가.
            //uriPattern을 전달, intent filter에 매칭하는 uri를 제공
            //adb 이용하여 테스트 가능.
            deepLinks = listOf(navDeepLink {
                uriPattern = "rally://$accountsName/{name}"
            })
        ) { entry ->
            //navBackStackEnrty의 매개 중 name
            val accountName = entry.arguments?.getString("name")
            val account = UserData.getAccount(accountName)

            //account를 singleAccountBody로 보냄 _ accounts패키지에 구현
            SingleAccountBody(account = account)
        }

    }
}

private fun navigateToSingleAccount(
    navController: NavHostController,
    accountName: String
) {
    navController.navigate("${Accounts.name}/$accountName")
}
