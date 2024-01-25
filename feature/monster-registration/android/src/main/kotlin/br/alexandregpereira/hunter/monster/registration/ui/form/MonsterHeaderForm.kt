package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.annotation.StringRes
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.res.stringResource
import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.monster.registration.R
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.PickerField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterHeaderForm(
    monster: Monster,
    onMonsterChanged: (Monster) -> Unit = {}
) {
    val key = "monsterHeader"
    FormLazy(
        key = key,
        title = { stringResource(R.string.monster_registration_edit) }
    ) {
        formItem(key = "$key-name") {
            AppTextField(
                text = monster.name,
                label = stringResource(R.string.monster_registration_name),
                onValueChange = { onMonsterChanged(monster.copy(name = it)) }
            )
        }
        formItem(key = "$key-subtitle") {
            AppTextField(
                text = monster.subtitle,
                label = stringResource(R.string.monster_registration_subtitle),
                onValueChange = { onMonsterChanged(monster.copy(subtitle = it)) }
            )
        }
        formItem(key = "$key-group") {
            AppTextField(
                text = monster.group.orEmpty(),
                label = stringResource(R.string.monster_registration_group),
                onValueChange = {
                    onMonsterChanged(monster.copy(group = it.takeUnless { it.isBlank() }))
                }
            )
        }
        formItem(key = "$key-imageUrl") {
            AppTextField(
                text = monster.imageData.url,
                label = stringResource(R.string.monster_registration_image_url),
                onValueChange = {
                    onMonsterChanged(monster.copy(imageData = monster.imageData.copy(url = it)))
                }
            )
        }
        formItem(key = "$key-imageBackgroundColor") {
            AppTextField(
                text = monster.imageData.backgroundColor.light,
                label = stringResource(R.string.monster_registration_image_background_color),
                onValueChange = {
                    onMonsterChanged(
                        monster.copy(imageData = monster.imageData.copy(
                            backgroundColor = Color(light = it, dark = it)
                        ))
                    )
                }
            )
        }
        formItem(key = "$key-type") {
            PickerField(
                value = stringResource(monster.type.toMonsterTypeState().stringRes),
                label = stringResource(R.string.monster_registration_type),
                options = MonsterType.entries.map {
                    stringResource(it.toMonsterTypeState().stringRes)
                },
                onValueChange = { i ->
                    onMonsterChanged(monster.copy(type = MonsterType.entries[i]))
                }
            )
        }
    }
}

private fun MonsterType.toMonsterTypeState(): MonsterTypeState {
    return when (this) {
        MonsterType.ABERRATION -> MonsterTypeState.ABERRATION
        MonsterType.BEAST -> MonsterTypeState.BEAST
        MonsterType.CELESTIAL -> MonsterTypeState.CELESTIAL
        MonsterType.CONSTRUCT -> MonsterTypeState.CONSTRUCT
        MonsterType.DRAGON -> MonsterTypeState.DRAGON
        MonsterType.ELEMENTAL -> MonsterTypeState.ELEMENTAL
        MonsterType.FEY -> MonsterTypeState.FEY
        MonsterType.FIEND -> MonsterTypeState.FIEND
        MonsterType.GIANT -> MonsterTypeState.GIANT
        MonsterType.HUMANOID -> MonsterTypeState.HUMANOID
        MonsterType.MONSTROSITY -> MonsterTypeState.MONSTROSITY
        MonsterType.OOZE -> MonsterTypeState.OOZE
        MonsterType.PLANT -> MonsterTypeState.PLANT
        MonsterType.UNDEAD -> MonsterTypeState.UNDEAD
    }
}

internal enum class MonsterTypeState(@StringRes val stringRes: Int) {
    ABERRATION(R.string.monster_registration_aberration),
    BEAST(R.string.monster_registration_beast),
    CELESTIAL(R.string.monster_registration_celestial),
    CONSTRUCT(R.string.monster_registration_construct),
    DRAGON(R.string.monster_registration_dragon),
    ELEMENTAL(R.string.monster_registration_elemental),
    FEY(R.string.monster_registration_fey),
    FIEND(R.string.monster_registration_fiend),
    GIANT(R.string.monster_registration_giant),
    HUMANOID(R.string.monster_registration_humanoid),
    MONSTROSITY(R.string.monster_registration_monstrosity),
    OOZE(R.string.monster_registration_ooze),
    PLANT(R.string.monster_registration_plant),
    UNDEAD(R.string.monster_registration_undead)
}
