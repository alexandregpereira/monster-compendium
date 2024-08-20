/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import br.alexandregpereira.hunter.monster.registration.SkillState
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.ui.compose.AppTextField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterProficiencyForm(
    keys: Iterator<String>,
    title: @Composable () -> String,
    proficiencies: List<SkillState>,
    onChanged: (List<SkillState>) -> Unit = {}
) {
    val mutableProficiencies = proficiencies.toMutableList()
    FormLazy(
        titleKey = keys.next(),
        title = title,
    ) {
        FormItems(
            keys = keys,
            items = mutableProficiencies,
            createNew = { SkillState() },
            onChanged = onChanged
        ) { i, proficiency ->
            formItem(key = keys.next()) {
                AppTextField(
                    text = proficiency.name,
                    label = strings.name,
                    onValueChange = { newValue ->
                        onChanged(mutableProficiencies.changeAt(i) { copy(name = newValue) })
                    }
                )
            }

            formItem(key = keys.next()) {
                AppTextField(
                    value = proficiency.modifier,
                    label = proficiency.name,
                    onValueChange = { newValue ->
                        onChanged(
                            mutableProficiencies.changeAt(i) {
                                copy(modifier = newValue)
                            }
                        )
                    }
                )
            }
        }
    }
}
