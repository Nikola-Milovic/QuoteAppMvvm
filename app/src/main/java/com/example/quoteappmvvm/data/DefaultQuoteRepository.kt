package com.example.quoteappmvvm.data

import com.example.quoteappmvvm.data.db.entity.BaseRepository
import com.example.quoteappmvvm.data.local.QuoteLocalDataSource
import com.example.quoteappmvvm.data.model.Quote
import com.example.quoteappmvvm.data.network.QuoteRemoteDataSource
import com.example.quoteappmvvm.di.ApplicationModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton



class DefaultQuoteRepository @Inject constructor(
    @ApplicationModule.QuoteRemoteDataSource private val quoteRemoteDataSource : QuoteDataSource,
    @ApplicationModule.QuoteLocalDataSource private val quoteLocalDataSource: QuoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseRepository(), QuoteRepository {


    override suspend fun getQuotes(): Result<List<Quote>> { // Fetch quotes from the quotes api
        // Remote first
        val remoteQuotes = quoteRemoteDataSource.getQuotes()
        when (remoteQuotes) {
            is Error -> return Result.Error(IOException("Error occurred during fetching quotes!"))
            is Result.Success -> {
                return remoteQuotes
            }
            else -> throw IllegalStateException()
        }
    }


}