/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.detail

import androidx.lifecycle.SavedStateHandle
import br.alexandregpereira.hunter.detail.ui.MonsterState

data class MonsterDetailViewState(
    val initialMonsterListPositionIndex: Int = 0,
    val monsters: List<MonsterState> = emptyList(),
    val showOptions: Boolean = false,
    val options: List<MonsterDetailOptionState> = emptyList(),
    val showDetail: Boolean = false,
    val isLoading: Boolean = true,
    val showCloneForm: Boolean = false,
    val monsterCloneName: String = "",
)

fun SavedStateHandle.getState(): MonsterDetailViewState {
    return MonsterDetailViewState(
        showDetail = this["showDetail"] ?: false,
        showCloneForm = this["showCloneForm"] ?: false,
        monsterCloneName = this["monsterCloneName"] ?: "",
    )
}

fun MonsterDetailViewState.saveState(savedStateHandle: SavedStateHandle): MonsterDetailViewState {
    savedStateHandle["showDetail"] = showDetail
    savedStateHandle["showCloneForm"] = showCloneForm
    savedStateHandle["monsterCloneName"] = monsterCloneName
    return this
}
