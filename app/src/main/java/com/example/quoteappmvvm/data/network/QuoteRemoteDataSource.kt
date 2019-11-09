package com.example.quoteappmvvm.data.network


import com.example.quoteappmvvm.data.QuoteDataSource
import com.example.quoteappmvvm.data.Result
import com.example.quoteappmvvm.data.model.Quote
import com.example.quoteappmvvm.data.model.QuoteApi
import java.io.IOException
import javax.inject.Inject


class QuoteRemoteDataSource @Inject constructor(
    private val apiService: QuoteApi
) : QuoteDataSource {
    
    override suspend fun getQuotes(): Result<List<Quote>> {
        val response = apiService.getQuotes().await()
        try {
            if (response.isSuccessful)
                return Result.Success(response.body()) as Result<List<Quote>>
            return Result.Error(IOException("Error occurred during fetching quotes!"))
        } catch (e: Exception) {
            Result.Error(IOException("Unable to fetch quotes!"))
        }
        return Result.Error(IOException("Unable to fetch quotes!"))
    }
}
