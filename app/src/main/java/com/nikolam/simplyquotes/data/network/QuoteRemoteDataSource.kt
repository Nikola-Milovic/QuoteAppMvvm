package com.nikolam.simplyquotes.data.network


import com.nikolam.simplyquotes.data.Result
import com.nikolam.simplyquotes.data.model.Quote
import javax.inject.Inject


class QuoteRemoteDataSource @Inject constructor(
    private val apiService: QuoteApi
) {

    // Fetch Quotes from Github, return a Result
    suspend fun getQuotes(): Result<List<Quote>> {
        return try {
            val quotes = apiService.getQuotes()
            Result.Success(quotes)
        } catch (e: Exception) {
            Result.Error(Exception("Cannot Fetch"))
        }

    }
}

