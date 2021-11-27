/*
 * Copyright 2020 Google LLC
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

package com.google.samples.apps.sunflower.plantdetail

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.viewmodels.PlantDetailViewModel


@Composable
fun PlantDetailDescription(plantDetailViewModel: PlantDetailViewModel) {
    /*composable에서 함수 관찰 시 : LiveData.observeAsState() 이용
        이 경우 livedata관찰 시작, state 개체를 통해 값을 나타냄.
        livedata에 새값이 게시될 경우 반환되는 state 값도 업데이트, 모든 state.value 값도 재구성됨.
     */
    //뷰모델로 넘어오는 필드값을 관찰함.
    val plant by plantDetailViewModel.plant.observeAsState()

   //nll값 검사용으로 한번 랩핑.
    plant?.let{
        PlantDetailContent(it)
    }

}

@Composable
fun PlantDetailContent(plant: Plant) {
    Surface {
        Column(Modifier.padding(dimensionResource(R.dimen.margin_normal))) {
            PlantName(plant.name)
            PlantWatering(plant.wateringInterval)
            PlantDescription(plant.description)
        }
    }
}

@Composable
private fun PlantName(name : String){
    Text(
        text = name,
        style = MaterialTheme.typography.h5, //xml 속성 : textAppearanceHeadline5
        modifier = Modifier
            .fillMaxWidth() //xml 속성 : match_parent
            .padding(horizontal = dimensionResource(R.dimen.margin_small)) //padding
            .wrapContentWidth(Alignment.CenterHorizontally) //수평으로 맞추기기
    )
}

@Composable
private fun PlantWatering(wateringInterval: Int) {
    Column(Modifier.fillMaxWidth()) {

        val centerWithPaddingModifier = Modifier
            .padding(horizontal = dimensionResource(R.dimen.margin_small))
            .align(Alignment.CenterHorizontally)

        val normalPadding = dimensionResource(R.dimen.margin_normal)

        Text(
            text = stringResource(R.string.watering_needs_prefix),
            color = MaterialTheme.colors.primaryVariant,
            fontWeight = FontWeight.Bold,
            modifier = centerWithPaddingModifier.padding(top = normalPadding)
        )

        // 현재 compose 버전에서 string 리소스 가져오기 지원x, localContext.current.resource로 접근함.
        // 이 부분은 인라인으로 작성했으나, 재사용할 수 있도록 다른 함수로 추출하는게 바람직.
        val wateringIntervalText = LocalContext.current.resources.getQuantityString(
            R.plurals.watering_needs_suffix, wateringInterval, wateringInterval
        )
        Text(
            text = wateringIntervalText,
            modifier = centerWithPaddingModifier.padding(bottom = normalPadding)
        )
    }
}

//원래 xml에서는 renderHtml : bindingAdpater를 통해 구현, html 태그를 포맷함.
// compose에서는 spanned 클래스를 지원X, 이 제한을 우회하기 위해 view system 이용. < androidView 사용
@Composable
private fun PlantDescription(description: String) {
    // html 포맷의 description을 기억, 새로 다시 실행하게함.
    val htmlDescription = remember(description) {
        HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    //화면에 텍스트뷰를 표시, 보기 확장 시 html 설명을 업데이트 (콜백) _ 이는 안드로이드뷰를 recompose 하고 텍스트를 업데이트함.
    //xml 파일을 삽입하고 십다면 viewbinding 이요하여 삽입 가능.
    AndroidView(
        factory = { context ->
            TextView(context).apply {
                movementMethod = LinkMovementMethod.getInstance()
            }
            //주어진 context를 사용하여 html 태그에 반응한 텍스트 뷰를 초기화
        },
        update = {
            it.text = htmlDescription
            //html 포맷에 설명으로 기억해놨던 애로 update해줌...?
        }
    )

    //비상이다 이 단락 다시 공부해보기.
}


@Preview
@Composable
private fun PlantWateringPreview() {
    MaterialTheme {
        PlantWatering(7)
    }
}

@Preview
@Composable
private fun PlantNamePreview() {
    MaterialTheme {
        PlantName("Apple")
    }
}

@Preview
@Composable
private fun PlantDetailContentPreview() {
    val plant = Plant("id", "Apple", "HTML<br><br>description", 3, 30, "")
    MaterialTheme {
        PlantDetailContent(plant)
    }
}

@Preview
@Composable
private fun PlantDescriptionPreview() {
    MaterialTheme {
        PlantDescription("HTML<br><br>description")
    }
}