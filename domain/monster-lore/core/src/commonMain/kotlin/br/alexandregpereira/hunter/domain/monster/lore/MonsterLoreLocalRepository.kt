/*
 * Copyright 2023 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.domain.monster.lore

import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore
import kotlinx.coroutines.flow.Flow

interface MonsterLoreLocalRepository {

    fun getMonsterLore(index: String): Flow<MonsterLore>

    fun getLocalMonstersLore(indexes: List<String>): Flow<List<MonsterLore>>

    fun getMonstersLoreEdited(): Flow<List<MonsterLore>>

    fun save(monstersLore: List<MonsterLore>, isSync: Boolean): Flow<Unit>
}
