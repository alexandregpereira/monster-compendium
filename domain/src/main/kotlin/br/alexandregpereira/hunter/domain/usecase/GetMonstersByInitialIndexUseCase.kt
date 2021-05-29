/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.domain.usecase

import br.alexandregpereira.hunter.domain.MonsterRepository
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.sort.sortMonstersByNameAndGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMonstersByInitialIndexUseCase(
    private val repository: MonsterRepository
) {

    operator fun invoke(index: String): Flow<Pair<Int, List<Monster>>> {
        return repository.getLocalMonsters().sortMonstersByNameAndGroup().map {
            val monster = it.find { monster -> monster.index == index }
                ?: throw IllegalAccessError("Monster not found")

            it.indexOf(monster) to it
        }
    }
}