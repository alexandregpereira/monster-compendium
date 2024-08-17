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

package br.alexandregpereira.hunter.domain.spell

import br.alexandregpereira.hunter.domain.spell.model.Spell
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

@OptIn(ExperimentalCoroutinesApi::class)
class SyncSpellsUseCase(
    private val repository: SpellRepository,
    private val spellSettingsRepository: SpellSettingsRepository,
    private val getSpellsEdited: GetSpellsEdited,
) {

    operator fun invoke(): Flow<Unit> {
        return spellSettingsRepository.getLanguage().flatMapLatest { lang ->
            repository.getRemoteSpells(lang)
        }.removeSpellsEdited().flatMapLatest { spells ->
            repository.deleteLocalSpells().flatMapLatest {
                repository.saveSpells(spells)
            }
        }
    }

    private fun Flow<List<Spell>>.removeSpellsEdited(): Flow<List<Spell>> = map { spells ->
        val spellsEditedIndexes = getSpellsEdited().single().map { it.index }.toSet()
        spells.filterNot { it.index in spellsEditedIndexes }
    }
}
