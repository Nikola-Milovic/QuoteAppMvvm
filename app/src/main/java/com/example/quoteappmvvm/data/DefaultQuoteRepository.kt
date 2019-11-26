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
) : QuoteRepository {


    override suspend fun getFavoriteQuotes(): Result<List<Quote>> {
        return when (val favoriteQuotes = quoteLocalDataSource.getFavoriteQuotes()) {
            is Error -> Result.Error(IOException("Error occurred during fetching favorite quotes!"))
            is Result.Success -> {
                if (favoriteQuotes.data.isNullOrEmpty()) {
                    Result.Error(IOException("Error occurred during fetching favorite quotes!"))
                } else {
                    favoriteQuotes
                }
            }
            else -> throw IllegalStateException()
        }
    }

    override suspend fun favoriteAQuote(quoteID: String) {
        quoteLocalDataSource.favoriteAQuote(quoteID)
    }


    override suspend fun fetchRemoteQuotes() = withContext(ioDispatcher) {
        quoteLocalDataSource.fetchRemoteQuotesAndInsertThemIntoDataBase()
    }

    override suspend fun getQuotesFromLocalDataBase(): Result<List<Quote>> { // Fetch quotes from local db
        return when (val quotes = quoteLocalDataSource.getQuotes()) {
            is Error -> Result.Error(IOException("Error occurred during fetching quotes!"))
            is Result.Success -> {
                if (quotes.data.isNullOrEmpty()) {
                    Result.Error(IOException("Error occurred during fetching quotes!"))
                } else {
                    quotes
                }
            }
            else -> throw IllegalStateException()
        }
    }


}