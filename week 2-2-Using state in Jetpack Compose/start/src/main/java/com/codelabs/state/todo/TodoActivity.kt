/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codelabs.state.todo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.codelabs.state.ui.StateCodelabTheme

class TodoActivity : AppCompatActivity() {
    //viewModel과 screen의 컴포저블 사이 다리 역할

    val todoViewModel by viewModels<TodoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StateCodelabTheme {
                Surface {
                    TodoActivityScreen(todoViewModel)
                }
            }
        }
    }

    @Composable
    private fun TodoActivityScreen(todoViewModel: TodoViewModel){

        /*
        //todoViewModel.todoItems는 viewModel의 liveData<List<ToDoItem>
        //observeAsState를 통해 liveData<T>를 관찰하고 이를 State<T>로 변환
        //이를 통해 compose는 값의 변화를 반영할 수 있음.
        //listOf()는 livedata 초기화되기 전 null 허용x로 넣은것. nullable하다면 items : List<TodoItem>?
        val items : List<TodoItem> by todoViewModel.todoItems.observeAsState(listOf())
        */

        TodoScreen(
            //items = items,
            items = todoViewModel.todoItems,
            currentlyEditing = todoViewModel.currentEditItem,
            onAddItem = todoViewModel::addItem,
            onRemoveItem = todoViewModel::removeItem,
            onStartEdit = todoViewModel::onEditItemSelected,
            onEditItemChange = todoViewModel::onEditItemChange,
            onEditDone = todoViewModel::onEditDone
        )
    }
}
