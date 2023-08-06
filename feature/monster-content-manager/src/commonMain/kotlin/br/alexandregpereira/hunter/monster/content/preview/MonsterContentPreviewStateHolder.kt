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

package br.alexandregpereira.hunter.monster.content.preview

import br.alexandregpereira.hunter.monster.compendium.domain.GetRemoteMonsterCompendiumUseCase
import br.alexandregpereira.hunter.monster.compendium.domain.getAlphabetIndexFromCompendiumItemIndex
import br.alexandregpereira.hunter.monster.compendium.domain.getCompendiumIndexFromTableContentIndex
import br.alexandregpereira.hunter.monster.compendium.domain.getTableContentIndexFromCompendiumItemIndex
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem
import br.alexandregpereira.hunter.monster.content.preview.MonsterContentPreviewAction.Companion.goToCompendiumIndex
import br.alexandregpereira.hunter.state.DefaultActionHandler
import br.alexandregpereira.hunter.state.DefaultMutableStateHolder
import br.alexandregpereira.hunter.state.MutableActionHandler
import br.alexandregpereira.hunter.state.MutableStateHolder
import br.alexandregpereira.hunter.state.ScopeManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MonsterContentPreviewStateHolder internal constructor(
    private val stateRecovery: MonsterContentPreviewStateRecovery,
    private val dispatcher: CoroutineDispatcher,
    private val analytics: MonsterContentPreviewAnalytics,
    private val getRemoteMonsterCompendiumUseCase: GetRemoteMonsterCompendiumUseCase,
    private val monsterContentPreviewEventManager: MonsterContentPreviewEventManager,
) : ScopeManager(),
    MutableStateHolder<MonsterContentPreviewState> by DefaultMutableStateHolder(stateRecovery),
    MutableActionHandler<MonsterContentPreviewAction> by DefaultActionHandler() {

    init {
        observeEvents()
        if (state.value.isOpen && state.value.monsterCompendiumItems.isEmpty()) {
            load()
        }
    }

    private fun observeEvents() {
        monsterContentPreviewEventManager.events
            .onEach { event ->
                when (event) {
                    is MonsterContentPreviewEvent.Show -> {
                        stateRecovery.sourceAcronym = event.sourceAcronym
                        stateRecovery.title = event.title
                        setState { open() }
                        load()
                    }
                }
            }
            .launchIn(scope)
    }

    private fun load() {
        setState { copy(isLoading = true) }
        getRemoteMonsterCompendiumUseCase(stateRecovery.sourceAcronym)
            .map { monsterCompendium ->
                val tableContent = monsterCompendium.tableContent
                val alphabet = monsterCompendium.alphabet
                MonsterContentPreviewState(
                    title = stateRecovery.title,
                    monsterCompendiumItems = monsterCompendium.items,
                    alphabet = monsterCompendium.alphabet,
                    tableContent = monsterCompendium.tableContent,
                    tableContentSelectedIndex = tableContent.getTableContentIndexFromCompendiumItemIndex(
                        itemIndex = 0,
                        items = monsterCompendium.items,
                    ),
                    alphabetSelectedIndex = alphabet.getAlphabetIndexFromCompendiumItemIndex(
                        itemIndex = 0,
                        items = monsterCompendium.items,
                    ),
                    isOpen = true,
                    isLoading = false
                )
            }
            .flowOn(dispatcher)
            .catch { error ->
                analytics.logException(error, stateRecovery.sourceAcronym)
                setState { copy(isLoading = false) }
            }
            .onEach { state ->
                analytics.trackLoaded(stateRecovery.sourceAcronym, state.monsterCompendiumItems)
                setState { state }
            }
            .launchIn(scope)
    }

    fun onClose() {
        analytics.trackClose(stateRecovery.sourceAcronym)
        setState { hide() }
    }

    fun onTableContentOpenButtonClick() {
        analytics.trackTableContentOpened()
        setState {
            copy(tableContentOpened = true)
        }
    }

    fun onTableContentClose() {
        analytics.trackTableContentClosed()
        setState {
            copy(tableContentOpened = false)
        }
    }

    fun onTableContentClick(tableContentIndex: Int) {
        val tableContent = state.value.tableContent
        analytics.trackTableContentIndexClicked(tableContentIndex, tableContent)
        setState { copy(tableContentOpened = false) }
        scope.launch {
            flowOf(state.value.monsterCompendiumItems)
                .getCompendiumIndexFromTableContentIndex(tableContent, tableContentIndex)
                .flowOn(dispatcher)
                .collect { compendiumIndex ->
                    sendAction(goToCompendiumIndex(compendiumIndex))
                }
        }
    }

    fun onFirstVisibleItemChange(compendiumItemIndex: Int) {
        val items = state.value.monsterCompendiumItems
        val alphabet = state.value.alphabet
        val tableContent = state.value.tableContent
        flowOf(compendiumItemIndex)
            .map {
                tableContent.getTableContentIndexFromCompendiumItemIndex(compendiumItemIndex, items) to
                        alphabet.getAlphabetIndexFromCompendiumItemIndex(compendiumItemIndex, items)
            }
            .flowOn(dispatcher)
            .onEach { (tableContentIndex, alphabetIndex) ->
                setState {
                    copy(
                        tableContentSelectedIndex = tableContentIndex,
                        alphabetSelectedIndex = alphabetIndex
                    )
                }
            }
            .launchIn(scope)
    }
}
