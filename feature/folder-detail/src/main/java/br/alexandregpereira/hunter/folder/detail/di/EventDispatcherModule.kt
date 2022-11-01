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

package br.alexandregpereira.hunter.folder.detail.di

import br.alexandregpereira.hunter.event.folder.detail.FolderDetailEventDispatcher
import br.alexandregpereira.hunter.event.folder.detail.FolderDetailResultListener
import br.alexandregpereira.hunter.folder.detail.FolderDetailEventManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class EventDispatcherModuleImpl {

    @Singleton
    @Provides
    internal fun provideEventDispatcherImpl(): FolderDetailEventManager {
        return FolderDetailEventManager()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class EventDispatcherModule {

    @Singleton
    @Binds
    internal abstract fun bindEventDispatcher(
        eventDispatcher: FolderDetailEventManager
    ): FolderDetailEventDispatcher

    @Singleton
    @Binds
    internal abstract fun bindResultListener(
        eventDispatcher: FolderDetailEventManager
    ): FolderDetailResultListener
}
