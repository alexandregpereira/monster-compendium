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

package br.alexandregpereira.hunter.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
fun Window(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = Window(darkTheme = isSystemInDarkTheme(), modifier = modifier, content = content)

@Composable
fun Window(
    darkTheme: Boolean = false,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = HunterTheme(darkTheme = darkTheme) {
    Surface(modifier = modifier, content = content)
}

fun Modifier.noIndicationClick(onClick: () -> Unit = {}): Modifier = composed {
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}
