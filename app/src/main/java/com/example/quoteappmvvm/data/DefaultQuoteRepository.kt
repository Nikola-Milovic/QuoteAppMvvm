package com.example.quoteappmvvm.data

import com.example.quoteappmvvm.data.model.Quote
import com.example.quoteappmvvm.di.ApplicationModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject


class DefaultQuoteRepository @Inject constructor(
    @ApplicationModule.QuoteLocalDataSource private val quoteLocalDataSource: QuoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) :  QuoteRepository {


    override suspend fun fetchQuotes() = withContext(ioDispatcher){
        quoteLocalDataSource.fetchRemoteQuotesAndInsertThemIntoDataBase()
    }

    override suspend fun getQuotes(): Result<List<Quote>> { // Fetch quotes from local db
        return when (val quotes = quoteLocalDataSource.getQuotes()) {
            is Error -> Result.Error(IOException("Error occurred during fetching quotes!"))
            is Result.Success -> {
                quotes
            }
            else -> throw IllegalStateException()
        }
    }


}