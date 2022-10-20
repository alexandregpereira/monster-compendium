/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.data.monster.local.mapper

import br.alexandregpereira.hunter.data.monster.local.entity.ProficiencyEntity
import br.alexandregpereira.hunter.domain.model.Proficiency

internal fun ProficiencyEntity.toDomain(): Proficiency {
    return Proficiency(index = this.index, modifier = this.modifier, name = this.name)
}

internal fun Proficiency.toEntity(monsterIndex: String): ProficiencyEntity {
    return ProficiencyEntity(
        index = this.index,
        modifier = this.modifier,
        name = this.name,
        monsterIndex = monsterIndex
    )
}
