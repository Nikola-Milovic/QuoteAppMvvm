package com.nikolam.simplyquotes.data

import com.nikolam.simplyquotes.data.model.Quote
import com.nikolam.simplyquotes.di.ApplicationModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject


class DefaultQuoteRepository @Inject constructor(
    @ApplicationModule.QuoteLocalDataSource private val quoteLocalDataSource: QuoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : QuoteRepository {

    override suspend fun deleteAllFavoriteQuotes() {
        quoteLocalDataSource.deleteAllFavoriteQuotes()
    }

    // Returns favorite quotes from the Local db, returns Success if succeeds but checks if the data returned is null or empty first
    override suspend fun getFavoriteQuotes(): Result<List<Quote>> {
        return when (val favoriteQuotes = quoteLocalDataSource.getFavoriteQuotes()) {
            is Error -> Result.Error(IOException("Error occurred during fetching favorite quotes!"))
            is Result.Success -> {
                if (favoriteQuotes.data.isNullOrEmpty()) {
                    Result.Error(IOException("Favorite Quotes Data is null or empty"))
                } else {
                    favoriteQuotes
                }
            }
            else -> throw IllegalStateException()
        }
    }

    override suspend fun favoriteAQuote(quoteID: Int) {
        quoteLocalDataSource.favoriteAQuote(quoteID)
    }

    override suspend fun unfavoriteAQuote(quoteID: Int) {
        quoteLocalDataSource.unfavoriteAQuote(quoteID)
    }



    override suspend fun fetchRemoteQuotes() = withContext(ioDispatcher) {
        quoteLocalDataSource.fetchRemoteQuotesAndInsertThemIntoDataBase()
    }

    override suspend fun getQuotesFromLocalDataBase(): Result<List<Quote>> { // Fetch quotes from local db
        return when (val quotes = quoteLocalDataSource.getQuotes()) {
            is Error -> Result.Error(IOException("Error occurred during fetching local quotes!"))
            is Result.Success -> {
                if (quotes.data.isNullOrEmpty()) {
                    Result.Error(IOException("Local Data is null or empty"))
                } else {
                    quotes
                }
            }
            else -> throw IllegalStateException()
        }
    }


}