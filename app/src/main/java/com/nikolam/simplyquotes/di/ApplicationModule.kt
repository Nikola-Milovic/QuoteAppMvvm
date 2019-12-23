package com.nikolam.simplyquotes.di

import android.content.Context
import androidx.room.Room
import com.nikolam.simplyquotes.data.DefaultQuoteRepository
import com.nikolam.simplyquotes.data.QuoteDataSource
import com.nikolam.simplyquotes.data.QuoteRepository
import com.nikolam.simplyquotes.data.local.LocalQuoteDataBase
import com.nikolam.simplyquotes.data.local.QuoteLocalDataSource
import com.nikolam.simplyquotes.data.network.JsonNetworkService
import com.nikolam.simplyquotes.data.network.QuoteRemoteDataSource
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
            quoteRemoteDataSource, database.quotesDao(), ioDispatcher
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
