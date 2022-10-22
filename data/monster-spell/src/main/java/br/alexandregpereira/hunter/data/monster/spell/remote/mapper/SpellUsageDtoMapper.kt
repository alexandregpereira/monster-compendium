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

package br.alexandregpereira.hunter.data.monster.spell.remote.mapper

import br.alexandregpereira.hunter.data.monster.spell.remote.model.SpellUsageDto
import br.alexandregpereira.hunter.domain.monster.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellPreview
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellUsage

fun List<SpellUsageDto>.toDomain(): List<SpellUsage> {
    return map { spellUsage ->
        SpellUsage(
            group = spellUsage.group,
            spells = spellUsage.spells.map { monsterSpell ->
                SpellPreview(
                    index = monsterSpell.index,
                    name = monsterSpell.name,
                    level = monsterSpell.level,
                    school = SchoolOfMagic.valueOf(monsterSpell.school.name),
                )
            }
        )
    }
}
