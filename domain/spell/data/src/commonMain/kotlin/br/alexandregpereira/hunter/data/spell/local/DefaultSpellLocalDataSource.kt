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

package br.alexandregpereira.hunter.data.spell.local

import br.alexandregpereira.hunter.data.spell.local.dao.SpellDao
import br.alexandregpereira.hunter.data.spell.local.model.SpellEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class DefaultSpellLocalDataSource(
    private val spellDao: SpellDao
) : SpellLocalDataSource {

    override fun saveSpells(spells: List<SpellEntity>): Flow<Unit> = flow {
        emit(spellDao.insert(spells))
    }

    override fun getSpell(index: String): Flow<SpellEntity> = flow {
        emit(spellDao.getSpell(index))
    }

    override fun deleteSpells(): Flow<Unit> = flow {
        emit(spellDao.deleteAll())
    }

    override fun getSpells(indexes: List<String>): Flow<List<SpellEntity>> = flow {
        emit(spellDao.getSpells(indexes))
    }

    override fun getSpells(): Flow<List<SpellEntity>> = flow {
        emit(spellDao.getSpells())
    }

    override fun getSpellsEdited(): Flow<List<SpellEntity>> = flow {
        emit(spellDao.getSpellsEdited())
    }
}
