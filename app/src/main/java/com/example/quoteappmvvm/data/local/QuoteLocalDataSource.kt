package com.example.quoteappmvvm.data.local

import com.example.quoteappmvvm.data.QuoteDataSource
import com.example.quoteappmvvm.data.Result
import com.example.quoteappmvvm.data.Result.Success
import com.example.quoteappmvvm.data.model.Quote
import kotlinx.coroutines.*
import java.io.IOException
import javax.inject.Inject


class QuoteLocalDataSource constructor(
    private val quotesDao: QuotesDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : QuoteDataSource{

    override suspend fun getQuotes(): Result<List<Quote>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(quotesDao.getQuotes())
        } catch (e: Exception) {
            Result.Error(IOException("Unable to fetch quotes!"))
        }
    }

}

/*  try {
            Success(tasksDao.getTasks())
        } catch (e: Exception) {
            Error(e)
        }

 */