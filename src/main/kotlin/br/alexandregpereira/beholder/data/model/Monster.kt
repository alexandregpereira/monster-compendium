package br.alexandregpereira.beholder.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.LinkedHashSet

@Serializable
data class Monster(
    @SerialName("id")
    val id: String,
    @SerialName("type")
    val type: MonsterType,
    @SerialName("name")
    val name: String,
    @SerialName("subtitle")
    val subtitle: String,
    @SerialName("image_url")
    val imageUrl: String?,
    @SerialName("size")
    val size: String,
    @SerialName("alignment")
    val alignment: String,
    @SerialName("subtype")
    val subtype: String?,
    @SerialName("armor_class")
    val armorClass: Int,
    @SerialName("hit_points")
    val hitPoints: Int,
    @SerialName("hit_dice")
    val hitDice: String,
    @SerialName("speed")
    val speeds: LinkedHashSet<Speed>,
    @SerialName("ability_scores")
    val abilityScores: LinkedHashSet<AbilityScore>
)

