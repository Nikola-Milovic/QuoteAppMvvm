package com.example.quoteappmvvm

import android.content.Context
import androidx.room.Room
import com.example.quoteappmvvm.data.DefaultQuoteRepository
import com.example.quoteappmvvm.data.QuoteDataSource
import com.example.quoteappmvvm.data.QuoteRepository
import com.example.quoteappmvvm.data.Result
import com.example.quoteappmvvm.data.local.LocalQuoteDataBase
import com.example.quoteappmvvm.data.local.QuoteLocalDataSource
import com.example.quoteappmvvm.data.model.Quote
import com.example.quoteappmvvm.data.model.QuoteApi
import com.example.quoteappmvvm.data.network.JsonNetworkService
import com.example.quoteappmvvm.data.network.QuoteRemoteDataSource
import com.example.quoteappmvvm.di.ApplicationModule
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


class MockQuoteDataSource : QuoteDataSource {
    override suspend fun fetchRemoteQuotesAndInsertThemIntoDataBase() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var result: Result<List<Quote>>
    override suspend fun getQuotes(): Result<List<Quote>> {
        return result
    }

    fun mockSuccess(quotes: List<Quote>) {
        result = Result.Success(quotes)
    }

    fun mockFailure(exception: Exception) {
        result = Result.Error(exception)
    }
}

@Module(includes = [TestApplicationModuleBinds::class])
object TestApplicationModule {

    val mockQuoteDataSource = MockQuoteDataSource()

    @JvmStatic
    @Provides
    fun provideQuoteApi(): QuoteApi {
        return mockk()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideMockDataSource(): MockQuoteDataSource {
        return mockQuoteDataSource
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideQuoteRemoteDataSource(
        jsonNetworkService: JsonNetworkService
    ): QuoteDataSource {
        return mockQuoteDataSource
    }


    @JvmStatic
    @Singleton
    @ApplicationModule.QuoteLocalDataSource
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
abstract class TestApplicationModuleBinds {

    @Singleton
    @Binds
    abstract fun bindRepository(repo: DefaultQuoteRepository): QuoteRepository
}
