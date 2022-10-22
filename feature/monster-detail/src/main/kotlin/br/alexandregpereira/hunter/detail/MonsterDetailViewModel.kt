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

package br.alexandregpereira.hunter.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.alexandregpereira.hunter.detail.domain.GetMonsterDetailUseCase
import br.alexandregpereira.hunter.detail.domain.model.MonsterDetail
import br.alexandregpereira.hunter.domain.model.MeasurementUnit
import br.alexandregpereira.hunter.domain.usecase.ChangeMonstersMeasurementUnitUseCase
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.Hide
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.Show
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventDispatcher
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventListener
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent.HideFolderPreview
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent.ShowFolderPreview
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventDispatcher
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEvent
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEventDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

@HiltViewModel
internal class MonsterDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMonsterDetailUseCase: GetMonsterDetailUseCase,
    private val changeMonstersMeasurementUnitUseCase: ChangeMonstersMeasurementUnitUseCase,
    private val folderPreviewEventDispatcher: FolderPreviewEventDispatcher,
    private val spellDetailEventDispatcher: SpellDetailEventDispatcher,
    private val monsterDetailEventListener: MonsterDetailEventListener,
    private val monsterDetailEventDispatcher: MonsterDetailEventDispatcher,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(savedStateHandle.getState())
    val state: StateFlow<MonsterDetailViewState> = _state

    private var monsterIndex: String
        get() = savedStateHandle["index"] ?: ""
        set(value) {
            savedStateHandle["index"] = value
        }

    private var monsterIndexes: List<String>
        get() = savedStateHandle["indexes"] ?: emptyList()
        set(value) {
            savedStateHandle["indexes"] = value
        }

    init {
        observeEvents()
        state.value.run {
            if (showDetail && monsters.isEmpty()) {
                getMonstersByInitialIndex(monsterIndex, monsterIndexes)
            }
        }
    }

    private fun observeEvents() {
        monsterDetailEventListener.events.onEach { event ->
            when (event) {
                is Show -> {
                    getMonstersByInitialIndex(event.index, event.indexes)
                    setState { copy(showDetail = true) }
                }
                Hide -> setState { copy(showDetail = false) }
            }
        }.launchIn(viewModelScope)
    }

    private fun getMonstersByInitialIndex(monsterIndex: String, monsterIndexes: List<String>) {
        folderPreviewEventDispatcher.dispatchEvent(HideFolderPreview)
        onMonsterChanged(monsterIndex)
        this.monsterIndexes = monsterIndexes
        setState { savedStateHandle.getState() }
        getMonsterDetail().collectDetail()
    }

    fun onMonsterChanged(monsterIndex: String) {
        this.monsterIndex = monsterIndex
    }

    fun onShowOptionsClicked() {
        setState { ShowOptions }
    }

    fun onShowOptionsClosed() {
        setState { HideOptions }
    }

    fun onOptionClicked(option: MonsterDetailOptionState) {
        setState { HideOptions }
        when (option) {
            MonsterDetailOptionState.CHANGE_TO_FEET -> {
                changeMeasurementUnit(MeasurementUnit.FEET)
            }
            MonsterDetailOptionState.CHANGE_TO_METERS -> {
                changeMeasurementUnit(MeasurementUnit.METER)
            }
        }
    }

    fun onSpellClicked(spellIndex: String) {
        spellDetailEventDispatcher.dispatchEvent(SpellDetailEvent.ShowSpell(spellIndex))
    }

    fun onClose() {
        monsterDetailEventDispatcher.dispatchEvent(Hide)
        folderPreviewEventDispatcher.dispatchEvent(ShowFolderPreview(force = false))
    }

    private fun getMonsterDetail(): Flow<MonsterDetail> {
        return getMonsterDetailUseCase(monsterIndex, indexes = monsterIndexes)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun changeMeasurementUnit(measurementUnit: MeasurementUnit) {
        changeMonstersMeasurementUnitUseCase(measurementUnit)
            .flatMapLatest { getMonsterDetail() }
            .collectDetail()
    }

    private fun Flow<MonsterDetail>.collectDetail() {
        this.map {
            Log.i("MonsterDetailViewModel", "collectDetail")
            val measurementUnit = it.measurementUnit
            getState().complete(
                initialMonsterIndex = it.monsterIndexSelected,
                monsters = it.monsters.asState(),
                options = when (measurementUnit) {
                    MeasurementUnit.FEET -> listOf(MonsterDetailOptionState.CHANGE_TO_METERS)
                    MeasurementUnit.METER -> listOf(MonsterDetailOptionState.CHANGE_TO_FEET)
                }
            )
        }.flowOn(dispatcher)
            .catch {
                Log.e("MonsterDetailViewModel", it.message ?: "")
                it.printStackTrace()
            }.onEach { state ->
                setState { state }
            }
            .launchIn(viewModelScope)
    }

    private suspend fun getState(): MonsterDetailViewState = withContext(Dispatchers.Main) {
        state.value
    }

    private fun setState(block: MonsterDetailViewState.() -> MonsterDetailViewState) {
        _state.value = state.value.block().saveState(savedStateHandle)
    }
}
