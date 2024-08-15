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

package br.alexandregpereira.hunter.search.domain

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterStatus
import br.alexandregpereira.hunter.domain.monster.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.domain.spell.GetSpellsByIdsUseCase
import br.alexandregpereira.hunter.domain.spell.model.Spell
import br.alexandregpereira.hunter.domain.usecase.GetMonsterPreviewsCacheUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonstersUseCase
import br.alexandregpereira.hunter.search.removeAccents
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single

internal class SearchMonstersByUseCase internal constructor(
    private val getMonsterPreviewsCacheUseCase: GetMonsterPreviewsCacheUseCase,
    private val getMonstersUseCase: GetMonstersUseCase,
    private val getSpellsByIdsUseCase: GetSpellsByIdsUseCase,
) {

    private val monsters: MutableList<Monster> = mutableListOf()

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(value: String, clearCache: Boolean): Flow<List<SearchMonsterResult>> {
        if (value.isBlank()) return flowOf(emptyList())

        if (clearCache) monsters.clear()
        return flow { emit(getSearchKeyValues(value).toMap()) }
            .flatMapLatest { searchKeyValueMap ->
                if (monsters.isNotEmpty()) return@flatMapLatest flowOf(monsters)
                when {
                    clearCache.not() && searchKeyValueMap.keys.all { it.hasOnPreview } -> {
                        getMonsterPreviewsCacheUseCase()
                    }

                    else -> {
                        getMonstersUseCase().appendSpellcastings().saveToSearchCache()
                    }
                }
            }
            .map { monsters ->
                val searchKeyValues = getSearchKeyValues(value)
                monsters.search(searchKeyValues.toMutableList())
            }
            .catch { error ->
                throw SearchMonstersByNameUnexpectedException(cause = error)
            }
            .map { monsters ->
                monsters.map { monster ->
                    SearchMonsterResult(
                        index = monster.index,
                        name = monster.name,
                        type = monster.type,
                        challengeRating = monster.challengeRatingFormatted,
                        imageUrl = monster.imageData.url,
                        backgroundColorLight = monster.imageData.backgroundColor.light,
                        backgroundColorDark = monster.imageData.backgroundColor.dark,
                        isHorizontalImage = monster.imageData.isHorizontal,
                    )
                }
            }
    }

    private fun List<Monster>.search(
        searchKeyValues: MutableList<Pair<SearchKey, String>>
    ): List<Monster> {
        if (searchKeyValues.isEmpty()) return this
        val (key, value) = searchKeyValues.first()
        searchKeyValues.removeFirst()
        return filter { monster ->
            monster.containsByKey(key, value)
        }.search(searchKeyValues.toMutableList())
    }

    private fun Flow<List<Monster>>.saveToSearchCache(): Flow<List<Monster>> {
        return onEach { monsters ->
            this@SearchMonstersByUseCase.monsters.clear()
            this@SearchMonstersByUseCase.monsters.addAll(monsters)
        }
    }

    //TODO It is duplicated with GetMonsterDetailUseCase logic
    private fun Flow<List<Monster>>.appendSpellcastings(): Flow<List<Monster>> {
        return map { monsters ->
            monsters to monsters.map { it.spellcastings }
                .takeIf { it.isNotEmpty() }
                ?.reduce { acc, values -> acc + values }
                ?.map { it.usages }
                ?.takeIf { it.isNotEmpty() }
                ?.reduce { acc, values -> acc + values }
                ?.map { it.spells }
                ?.takeIf { it.isNotEmpty() }
                ?.reduce { acc, values -> acc + values }
                ?.map { it.index }
                ?.let { spellIndexes ->
                    getSpellsByIdsUseCase(spellIndexes).single()
                }.orEmpty()
        }.map { (monsters, spells) ->
            monsters.appendSpells(spells)
        }
    }

    //TODO It is duplicated with GetMonsterDetailUseCase logic
    private fun List<Monster>.appendSpells(spells: List<Spell>): List<Monster> {
        return map { monstersWithSpells ->
            monstersWithSpells.copy(
                spellcastings = monstersWithSpells.spellcastings.mapNotNull { spellcasting ->
                    spellcasting.copy(
                        usages = spellcasting.usages.mapNotNull { spellUsage ->
                            spellUsage.copy(
                                spells = spellUsage.spells.mapNotNull { spellPreview ->
                                    spells.find { spellPreview.index == it.index }?.run {
                                        spellPreview.copy(
                                            name = name,
                                            level = level,
                                            school = SchoolOfMagic.valueOf(school.name)
                                        )
                                    }
                                }
                            ).takeIf { it.spells.isNotEmpty() }
                        }
                    ).takeIf { it.usages.isNotEmpty() }
                }
            )
        }
    }

    private fun getSearchKeyValues(name: String): List<Pair<SearchKey, String>> {
        return name.split("&").map { it.trim() }.map { valueWithSymbols ->
            val searchKey = SearchKey.entries.find {
                valueWithSymbols.startsWith(it.key, ignoreCase = true)
            } ?: SearchKey.Name

            when (searchKey.valueType) {
                SearchValueType.String -> {
                    searchKey.getSearchKeyValue(delimiter = "=", valueWithSymbols)
                }
                SearchValueType.Boolean -> searchKey to "true"
                SearchValueType.Float -> {
                    val delimiter = when {
                        valueWithSymbols.contains(">") -> ">"
                        valueWithSymbols.contains("<") -> "<"
                        else -> "="
                    }
                    searchKey.getSearchKeyValue(delimiter, valueWithSymbols)
                }
            }
        }
    }

    private fun SearchKey.getSearchKeyValue(
        delimiter: String,
        valueWithSymbols: String
    ): Pair<SearchKey, String> {
        val valueSplit = valueWithSymbols.split(delimiter)
        val value = valueSplit.lastOrNull()
        return if (value == null) SearchKey.Name to valueWithSymbols
        else this to "$delimiter$value"
    }

    private fun Monster.containsByKey(searchKey: SearchKey, valueWithDelimiter: String): Boolean {
        val delimiter = valueWithDelimiter.first().toString()
        val value = when (delimiter) {
            "=", ">", "<" -> valueWithDelimiter.removePrefix(delimiter)
            else -> valueWithDelimiter
        }
        val valueWithNoAccents = value.removeAccents().takeIf { it.isNotBlank() } ?: return false
        return when (searchKey) {
            SearchKey.Name -> containsByName(valueWithNoAccents)
            SearchKey.Type -> containsByType(valueWithNoAccents)
            SearchKey.Cr -> containsByChallengeRating(valueWithNoAccents, delimiter)
            SearchKey.Spell -> containsBySpell(valueWithNoAccents)
            SearchKey.Legendary -> containsByLegendary()
            SearchKey.Source -> containsBySource(valueWithNoAccents)
            SearchKey.Edited -> containsByEdited()
            SearchKey.Cloned -> containsByCloned()
            SearchKey.Imported -> containsByImported()
        }
    }

    private fun Monster.containsByName(value: String): Boolean {
        return name.removeAccents().contains(value, ignoreCase = true) ||
                index.replace("-", " ").contains(value, ignoreCase = true)
    }

    private fun Monster.containsByType(value: String): Boolean {
        return type.name.removeAccents().contains(value, ignoreCase = true)
    }

    private fun Monster.containsByChallengeRating(value: String, delimiter: String): Boolean {
        val crValue = value.toFloatOrNull() ?: return false
        return when (delimiter) {
            ">" -> return challengeRating > crValue
            "<" -> return challengeRating < crValue
            else -> return challengeRating == crValue
        }
    }

    private fun Monster.containsBySpell(value: String): Boolean {
        return spellcastings.map { it.usages }
            .takeIf { it.isNotEmpty() }
            ?.reduce { acc, spellUsages -> acc + spellUsages }
            ?.map { it.spells }
            ?.takeIf { it.isNotEmpty() }
            ?.reduce { acc, spellPreviews -> acc + spellPreviews }
            ?.any { spellPreview ->
                spellPreview.name.removeAccents()
                    .contains(value, ignoreCase = true) ||
                        spellPreview.index.replace("-", " ")
                            .contains(value, ignoreCase = true)
            } ?: false
    }

    private fun Monster.containsByLegendary(): Boolean {
        return legendaryActions.isNotEmpty()
    }

    private fun Monster.containsBySource(value: String): Boolean {
        return sourceName.removeAccents().contains(value, ignoreCase = true)
    }

    private fun Monster.containsByEdited(): Boolean {
        return status == MonsterStatus.Edited
    }

    private fun Monster.containsByCloned(): Boolean {
        return status == MonsterStatus.Clone
    }

    private fun Monster.containsByImported(): Boolean {
        return status == MonsterStatus.Imported
    }
}
