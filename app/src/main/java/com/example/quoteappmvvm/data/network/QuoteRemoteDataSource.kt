package com.example.quoteappmvvm.data.network


import com.example.quoteappmvvm.data.Result
import com.example.quoteappmvvm.data.model.Quote
import com.example.quoteappmvvm.data.model.QuoteApi
import javax.inject.Inject


class QuoteRemoteDataSource @Inject constructor(
    private val apiService: QuoteApi
) {


    suspend fun getQuotes(): Result<List<Quote>> {
        return try {
            val quotes = apiService.getQuotes()
            Result.Success(quotes)
        } catch (e: Exception) {
            Result.Error(Exception("Cannot Fetch"))
        }

    }
}

