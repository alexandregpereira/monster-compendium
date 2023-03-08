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

package br.alexandregpereira.hunter.event.monster.detail

import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.OnMonsterPageChanges
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.OnVisibilityChanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

interface MonsterDetailEventListener {

    val events: Flow<MonsterDetailEvent>
}

fun emptyMonsterDetailEventListener(): MonsterDetailEventListener {
    return object : MonsterDetailEventListener {
        override val events: Flow<MonsterDetailEvent>
            get() = emptyFlow()
    }
}

fun MonsterDetailEventListener.collectOnVisibilityChanges(
    action: suspend (OnVisibilityChanges) -> Unit
): Flow<OnVisibilityChanges> {
    return events.map { it as? OnVisibilityChanges }.filterNotNull().onEach(action)
}

fun MonsterDetailEventListener.collectOnMonsterPageChanges(
    action: (OnMonsterPageChanges) -> Unit
): Flow<Unit> {
    return events.map { it as? OnMonsterPageChanges }.filterNotNull().map(action)
}
