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

package br.alexandregpereira.hunter.data.local.mapper

import br.alexandregpereira.hunter.data.local.entity.SavingThrowEntity
import br.alexandregpereira.hunter.domain.model.SavingThrow

internal fun List<SavingThrowEntity>.toDomain(): List<SavingThrow> {
    return this.map {
        SavingThrow(type = it.type.toDomain(), modifier = it.modifier)
    }
}

internal fun List<SavingThrow>.toEntity(): List<SavingThrowEntity> {
    return this.map {
        SavingThrowEntity(type = it.type.toEntity(), modifier = it.modifier)
    }
}