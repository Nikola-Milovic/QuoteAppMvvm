package com.example.quoteappmvvm.data.local

import android.util.Log
import com.example.quoteappmvvm.data.QuoteDataSource
import com.example.quoteappmvvm.data.Result
import com.example.quoteappmvvm.data.Result.Success
import com.example.quoteappmvvm.data.model.Quote
import com.example.quoteappmvvm.data.network.QuoteRemoteDataSource
import com.example.quoteappmvvm.di.ApplicationModule
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

    override suspend fun getQuotes(): Result<List<Quote>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(quotesDao.getQuotes())
        } catch (e: Exception) {
            Result.Error(IOException("Unable to fetch quotes!"))
        }
    }
}
