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

package br.alexandregpereira.hunter.ui.compendium

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.SectionTitle

@Composable
fun Compendium(
    items: List<CompendiumItemState>,
    modifier: Modifier = Modifier,
    animateItems: Boolean = false,
    columns: CompendiumColumns = CompendiumColumns.Fixed(count = 2),
    listState: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    key: (CompendiumItemState.Item) -> Any? = { null },
    isHorizontalCard: (CompendiumItemState.Item) -> Boolean = { false },
    titleLineSpan: LazyGridItemSpanScope.() -> Int? = { null },
    cardContent: @Composable (CompendiumItemState.Item) -> Unit,
) = LazyVerticalGrid(
    columns = columns.toGridCells(),
    state = listState,
    contentPadding = PaddingValues(
        start = 16.dp,
        end = 16.dp,
        top = contentPadding.calculateTopPadding(),
        bottom = contentPadding.calculateBottomPadding() + 64.dp
    ),
    horizontalArrangement = Arrangement.spacedBy(
        space = 16.dp,
        alignment = Alignment.CenterHorizontally
    ),
    modifier = modifier,
) {
    items.forEachIndexed { index, item ->
        val sectionTitlePaddingTop = 32.dp
        val sectionTitlePaddingBottom = 16.dp

        when (item) {
            is CompendiumItemState.Title -> {
                item(
                    key = item.id,
                    span = {
                        val lineSpan = when {
                            index == 0 -> maxLineSpan
                            else -> titleLineSpan()
                        }
                        GridItemSpan(currentLineSpan = lineSpan ?: maxLineSpan)
                    }
                ) {
                    val paddingTop = when {
                        item.isHeader -> sectionTitlePaddingTop
                        else -> 24.dp
                    }
                    SectionTitle(
                        title = item.value,
                        isHeader = item.isHeader,
                        modifier = Modifier
                            .animateItems(this, animateItems)
                            .padding(
                                top = paddingTop,
                                bottom = sectionTitlePaddingBottom
                            )
                    )
                }
            }

            is CompendiumItemState.Item -> {
                item(
                    key = key(item),
                    span = {
                        val lineSpan = if (isHorizontalCard(item)) 2 else 1
                        GridItemSpan(lineSpan)
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .animateItems(this, animateItems)
                            .padding(vertical = 8.dp),
                    ) {
                        cardContent(item)
                    }
                }
            }
        }
    }
}

sealed class CompendiumColumns {
    data class Fixed(val count: Int) : CompendiumColumns()

    data class Adaptive(
        val minSize: Int,
    ) : CompendiumColumns()

    class FixedSize(val size: Dp) : CompendiumColumns()
}

private fun CompendiumColumns.toGridCells(): GridCells = when (this) {
    is CompendiumColumns.Fixed -> GridCells.Fixed(count)
    is CompendiumColumns.Adaptive -> GridCells.Adaptive(minSize.dp)
    is CompendiumColumns.FixedSize -> GridCells.FixedSize(size)
}

@OptIn(ExperimentalFoundationApi::class)
private fun Modifier.animateItems(
    scope: LazyGridItemScope,
    animateItems: Boolean
): Modifier = scope.run {
    if (animateItems) this@animateItems.animateItemPlacement() else this@animateItems
}
