package com.nikolam.simplyquotes.data.local

import android.util.Log
import com.nikolam.simplyquotes.data.QuoteDataSource
import com.nikolam.simplyquotes.data.Result
import com.nikolam.simplyquotes.data.Result.Success
import com.nikolam.simplyquotes.data.model.Quote
import com.nikolam.simplyquotes.data.network.QuoteRemoteDataSource
import com.nikolam.simplyquotes.di.ApplicationModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject


class QuoteLocalDataSource @Inject constructor( // for fetching locally stored quotes, might save favorite quootes or save the json quotes
    @ApplicationModule.QuoteLocalDataSource private val quoteRemoteDataSource: QuoteRemoteDataSource,
    private val quotesDao: QuotesDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : QuoteDataSource {
    override suspend fun deleteAllFavoriteQuotes() {
        quotesDao.unfavoriteAllQuote()
    }

    // Fetch favorite quotes from local db
    override suspend fun getFavoriteQuotes(): Result<List<Quote>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(quotesDao.getFavoriteQuotes())
        } catch (e: Exception) {
            Result.Error(IOException("Unable to fetch quotes!"))
        }
    }

    override suspend fun favoriteAQuote(quoteID: Int) {
        quotesDao.favoriteAQuote(quoteID)
    }

    override suspend fun unfavoriteAQuote(quoteID: Int) {
        quotesDao.unfavoriteAQuote(quoteID)
    }


    // Fetch Quotes from Github and insert them into Local db, if the fetching succeeds then it inserts, otherwise throw exception
    override suspend fun fetchRemoteQuotesAndInsertThemIntoDataBase() {
        try {
            when (val quotes = quoteRemoteDataSource.getQuotes()) {
                is Success -> quotesDao.insertQuotes(quotes.data)
                is Result.Error -> throw IllegalStateException()
                is Result.Loading -> Log.d("TAG", "Quotes loading")
            }
        } catch (e: Exception) {
            Log.d("TAG", e.message.toString())
            throw IllegalStateException()
        }
    }

    // Fetch quotes from Local db
    override suspend fun getQuotes(): Result<List<Quote>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(quotesDao.getQuotes())
        } catch (e: Exception) {
            Result.Error(IOException("Unable to fetch quotes!"))
        }
    }
}
