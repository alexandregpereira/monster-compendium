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

package br.alexandregpereira.hunter.domain.sync

import br.alexandregpereira.hunter.domain.settings.GetLanguageUseCase
import br.alexandregpereira.hunter.domain.settings.IsLanguageSupported
import br.alexandregpereira.hunter.domain.settings.SaveLanguageUseCase
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class HandleLanguageUseCase @Inject internal constructor(
    private val sync: SyncUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val saveLanguageUseCase: SaveLanguageUseCase
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Unit> {
        return getLanguageUseCase().flatMapLatest { lang ->
            val deviceLang = Locale.getDefault().toLanguageTag().lowercase()
            if (deviceLang != lang && IsLanguageSupported(deviceLang)) {
                saveLanguageUseCase(deviceLang).flatMapLatest {
                    sync()
                }
            } else flowOf(Unit)
        }
    }
}
