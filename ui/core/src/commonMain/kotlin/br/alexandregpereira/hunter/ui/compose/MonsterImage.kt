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

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.resources.Res
import br.alexandregpereira.hunter.ui.resources.ic_aberration
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import br.alexandregpereira.hunter.ui.theme.Shapes
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MonsterImage(
    url: String,
    icon: DrawableResource,
    backgroundColor: String,
    challengeRating: String,
    modifier: Modifier = Modifier,
    contentDescription: String = "",
) {
    val shape = Shapes.large
    val iconSize = 24.dp
    val challengeRatingSize = 48.dp
    val challengeRatingFontSize = 14.sp
    Box(
        modifier.clip(shape)
    ) {
        MonsterCoilImage(
            imageUrl = url,
            contentDescription = contentDescription,
            backgroundColor = backgroundColor,
            shape = shape,
        )

        ChallengeRatingCircle(
            challengeRating = challengeRating,
            size = challengeRatingSize,
            fontSize = challengeRatingFontSize
        )

        MonsterTypeIcon(
            icon = icon,
            iconSize = iconSize,
            tint = backgroundColor.getTintColor(),
            modifier = Modifier.align(Alignment.TopEnd),
        )
    }
}

@Preview
@Composable
fun MonsterImagePreview() = HunterTheme {
    MonsterImage(
        url = "asdasdas",
        backgroundColor = "#ffe3ee",
        contentDescription = "Anything",
        challengeRating = "18",
        icon = Res.drawable.ic_aberration
    )
}

@Preview
@Composable
fun MonsterImageBlackBackgroundPreview() = HunterTheme {
    MonsterImage(
        url = "asdasdas",
        backgroundColor = "#000000",
        contentDescription = "Anything",
        challengeRating = "18",
        icon = Res.drawable.ic_aberration
    )
}
