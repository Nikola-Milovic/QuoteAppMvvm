package com.example.quoteappmvvm.data.network


import java.io.IOException
import javax.inject.Inject
import com.example.quoteappmvvm.data.Result
import com.example.quoteappmvvm.data.model.QuoteResponse


class QuoteDataSource @Inject constructor (
    private val apiService : JsonNetworkService) {


    suspend fun getMovies(): Result<QuoteResponse> {
        val response = apiService.getServiceApi()
        if (response.isSuccessful)
            return Result.Success(response.body())
        return Result.Error(IOException("Error occurred during fetching movies!"))

    }
}

