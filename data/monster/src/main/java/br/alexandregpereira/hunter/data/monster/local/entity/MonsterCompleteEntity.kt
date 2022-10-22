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

package br.alexandregpereira.hunter.data.monster.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellcastingCompleteEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellcastingEntity

data class MonsterCompleteEntity(
    @Embedded val monster: MonsterEntity,
    @Relation(
        entity = SpeedEntity::class,
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val speed: SpeedWithValuesEntity?,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val abilityScores: List<AbilityScoreEntity>,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val savingThrows: List<SavingThrowEntity>,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val skills: List<SkillEntity>,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val damageVulnerabilities: List<DamageVulnerabilityEntity>,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val damageResistances: List<DamageResistanceEntity>,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val damageImmunities: List<DamageImmunityEntity>,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val conditionImmunities: List<ConditionEntity>,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val specialAbilities: List<SpecialAbilityEntity>,
    @Relation(
        entity = ActionEntity::class,
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val actions: List<ActionWithDamageDicesEntity>,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val reactions: List<ReactionEntity>,
    @Relation(
        entity = SpellcastingEntity::class,
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val spellcastings: List<SpellcastingCompleteEntity>,
)
