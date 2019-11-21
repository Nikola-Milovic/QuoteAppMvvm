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


//    return apiService.getQuotes().body()
//}

/* try {
            if (response.isSuccessful)
                if(!response.body().isNullOrEmpty())
                return Result.Success(response.body()) as Result1<List<Quote>>
            return Result.Error(IOException("Error occurred during fetching quotes!"))
        } catch (e: Exception) {
            Result.Error(IOException("Unable to fetch quotes!"))
        }
        return Result.Error(IOException("Unable to fetch quotes!"))*/