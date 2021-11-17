# ComposeFest2021
Using state in Jetpack Compose 본 폴더를 Android Studio를 이용해서 열어주세요.
작업을 완료하고, push 해주세요.

1. 영상 보러가기

    [![Video Label](https://img.youtube.com/vi/XXKmlKolcPk/0.jpg)](https://youtu.be/XXKmlKolcPk)

2. 슬라이드 자료 보러가기 👉 [링크](https://speakerdeck.com/veronikapj/2021-composefest2021-using-state-in-jetpack-compose)


<br/><br/>
# Using State in Jetpack Compose Codelab

This folder contains the source code for the [Using State in Jetpack Compose codelab](https://developer.android.com/codelabs/jetpack-compose-state).


In this codelab, you will explore patterns for working with state in a declarative world by building a Todo application. We'll see what unidirectional
data flow is, and how to apply it in a Jetpack Compose application to build stateless and stateful composables.

## Screenshots

![Finished code](screenshots/state_movie.gif "After: Animation of fully completed project")

## License

```
Copyright 2020 The Android Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```



//메모메모,,,,
기존 아키텍처 : view 조회 후 속성 설정 필요 _ 상태 변경 시 ui와 동기화 해야함.
- 동기화 과정에서 버그 나기도 쉽고, 코드가 복잡 ->결합도가 높아지게 됨. 

compose : 선언형 ui 모델 및 단방향 데이터 흐름으로 전환, 데이터를 바꿀때만 업데이트하면 됨.
state란? : 시간이 지남에 따라 변경될 수 있는 모든 값
EX 1. 네트워크 연결을 할 수 없을 때 보여주는 스낵바
2. 블로그 포스트와 관련 댓글
3. 클릭할 때 재생되는 ripple 애니메이션
4. 사용자가 이미지 위에 그릴 수 있는 스티커

단방향 데이터 흐름 > state 다루는 법
일반적 이벤트 흐름 : event -> update stae -> diplay state -> event
테스트 하기 어렵고, 얽힌 상태와 ui 일일이 update > 깨진 ui 발생 O

단방향 흐름: viewModel -> (state) -> activity / viewModel <- (event) <- activity

compose에서 프로그래밍 할 때 알아야할 사항 
1. compose함수는 순서와 관계없이 실행 가능
2. compsoe는 동시에 실행 가능
   -> 1.2때문에 각 함수는 독립적이야함.
3. recomposition(재구성)은 변경된 구성 요소만 실행 
4. 같은 데이터라면 동일한 결과가 나와야함. (3때문에 효율적으로 실행하려면,,)
5. compose 함수는 매우 자주 실행될 수 있는데, 이는 모든 ui에서 그릴 수 있기 때문에
   비용이 많이 드는 작업을 compose에서 실행시 버벅거릴 수 있음.
   요런 작업은 외부에 다른 스레드에서 작업해야함. 
   
state 적용해 활용방법 :
- compose는 state가 update된 것만 다시 그리는게 효율적임
이를 위해서 remember을 사용. composition tree에 상태 저장, 키가 변경될 때만 다시 계산
  따라서 remember을 적용하고 recomposition이 발생 시 이전 생성값을 반환. 
  즉 상태가 변경된 것만 다시 그릴 수 있음. 
  //rember을 사용해 내부 state를 생성한 composable을 stateful ( stateful은 호출 쪽에서 state관리X, 재사용 가능성이 적음, 테스트 힘듬)
  내부 state를 갖지 않는 composable을 staetless라고 함. (stateless는 state를 호출해야하는 곳에서 관리, 제어함. 재사용성이나 테스트 더 좋음.)
  > 따라서 stateful에서 재사용성이나 테스트 용이성을 높이기 위해 stateless로 만드는게 중요 
  
이러한 방법 중 하나가 state hoisting
  > state hoisting 시 어디로 가야하는지 알아내는데 도움이 되는 세 가지 규칙.
  > stae는 staet를 사용하는 모든 composable의 최소 공통 상위 항목으로
  > state는 최소한 수정할 수 있는 최고 수준으로
  > 동일한 이벤트에 대한 응답으로 두 state가 변경되면 함께
