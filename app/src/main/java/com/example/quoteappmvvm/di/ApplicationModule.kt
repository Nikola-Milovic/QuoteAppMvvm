/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.quoteappmvvm.di

import android.content.Context
import androidx.room.Room
import com.example.quoteappmvvm.data.DefaultQuoteRepository
import com.example.quoteappmvvm.data.QuoteDataSource
import com.example.quoteappmvvm.data.QuoteRepository
import com.example.quoteappmvvm.data.local.LocalQuoteDataBase
import com.example.quoteappmvvm.data.local.QuoteLocalDataSource
import com.example.quoteappmvvm.data.network.JsonNetworkService
import com.example.quoteappmvvm.data.network.QuoteRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.annotation.AnnotationRetention.RUNTIME


@Module(includes = [ApplicationModuleBinds::class])
object ApplicationModule {



    @Qualifier
    @Retention(RUNTIME)
    annotation class QuoteLocalDataSource

    @JvmStatic
    @Singleton
    @Provides
    fun provideQuoteRemoteDataSource(
        jsonNetworkService: JsonNetworkService
    ): QuoteRemoteDataSource {
        return QuoteRemoteDataSource(jsonNetworkService.apiService)
    }

    @JvmStatic
    @Singleton
    @QuoteLocalDataSource
    @Provides
    fun provideQuoteLocalDataSource(
        quoteRemoteDataSource: QuoteRemoteDataSource,
        database: LocalQuoteDataBase,
        ioDispatcher: CoroutineDispatcher
    ): QuoteDataSource {
        return QuoteLocalDataSource(
            quoteRemoteDataSource ,database.quotesDao(), ioDispatcher
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideDataBase(context: Context): LocalQuoteDataBase {
        return Room.databaseBuilder(
            context.applicationContext,
            LocalQuoteDataBase::class.java,
            "quote.db"
        ).build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}

@Module
abstract class ApplicationModuleBinds {

    @Singleton
    @Binds
    abstract fun bindRepository(repo: DefaultQuoteRepository): QuoteRepository
}
