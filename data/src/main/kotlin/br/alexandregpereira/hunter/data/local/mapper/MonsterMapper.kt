package br.alexandregpereira.hunter.data.local.mapper

import br.alexandregpereira.hunter.data.local.entity.AbilityScoreEntity
import br.alexandregpereira.hunter.data.local.entity.DamageEntity
import br.alexandregpereira.hunter.data.local.entity.MonsterEntity
import br.alexandregpereira.hunter.data.local.entity.SavingThrowEntity
import br.alexandregpereira.hunter.data.local.entity.SkillEntity
import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.Stats

internal fun List<MonsterEntity>.toDomain(): List<Monster> {
    return this.map {
        Monster(
            index = it.index,
            type = MonsterType.valueOf(it.type),
            subtype = it.subtype,
            group = it.group,
            challengeRating = it.challengeRating,
            name = it.name,
            subtitle = it.subtitle,
            imageData = MonsterImageData(
                url = it.imageUrl,
                backgroundColor = Color(
                    light = it.backgroundColorLight,
                    dark = it.backgroundColorDark
                ),
                isHorizontal = it.isHorizontalImage
            ),
            size = it.size,
            alignment = it.alignment,
            stats = Stats(
                armorClass = it.armorClass,
                hitPoints = it.hitPoints,
                hitDice = it.hitDice
            ),
            speed = it.speedEntity.toDomain(),
            abilityScores = it.abilityScores.toListFromJson<AbilityScoreEntity>().toDomain(),
            savingThrows = it.savingThrows.toListFromJson<SavingThrowEntity>().toDomain(),
            skills = it.skills.toListFromJson<SkillEntity>().toDomain(),
            damageVulnerabilities = it.damageVulnerabilities.toListFromJson<DamageEntity>()
                .toDomain(),
            damageResistances = it.damageResistances.toListFromJson<DamageEntity>().toDomain(),
            damageImmunities = it.damageImmunities.toListFromJson<DamageEntity>().toDomain()
        )
    }
}

internal fun List<Monster>.toEntity(): List<MonsterEntity> {
    return this.map {
        MonsterEntity(
            index = it.index,
            type = it.type.name,
            subtype = it.subtype,
            group = it.group,
            challengeRating = it.challengeRating,
            name = it.name,
            subtitle = it.subtitle,
            imageUrl = it.imageData.url,
            backgroundColorLight = it.imageData.backgroundColor.light,
            backgroundColorDark = it.imageData.backgroundColor.dark,
            isHorizontalImage = it.imageData.isHorizontal,
            size = it.size,
            alignment = it.alignment,
            armorClass = it.stats.armorClass,
            hitPoints = it.stats.hitPoints,
            hitDice = it.stats.hitDice,
            speedEntity = it.speed.toEntity(),
            abilityScores = it.abilityScores.toEntity().toJsonFromList(),
            savingThrows = it.savingThrows.toEntity().toJsonFromList(),
            skills = it.skills.toEntity().toJsonFromList(),
            damageVulnerabilities = it.damageVulnerabilities.toEntity().toJsonFromList(),
            damageResistances = it.damageResistances.toEntity().toJsonFromList(),
            damageImmunities = it.damageImmunities.toEntity().toJsonFromList()
        )
    }
}