/*
 * Copyright 2023 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.domain.settings

import br.alexandregpereira.hunter.domain.settings.SaveContentVersionUseCase.Companion.CONTENT_VERSION_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetContentVersionUseCase(
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(): Flow<Int> {
        return settingsRepository.getSettingsValue(
            key = CONTENT_VERSION_KEY,
            defaultValue = "0"
        ).map { it.toInt() }
    }
}
