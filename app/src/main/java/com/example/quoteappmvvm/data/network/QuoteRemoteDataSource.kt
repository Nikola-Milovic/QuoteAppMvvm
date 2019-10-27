package com.example.quoteappmvvm.data.network


import com.example.quoteappmvvm.data.QuoteDataSource
import java.io.IOException
import javax.inject.Inject
import com.example.quoteappmvvm.data.Result
import com.example.quoteappmvvm.data.model.Quote
import com.example.quoteappmvvm.data.model.QuoteApi



class QuoteRemoteDataSource @Inject constructor (
    private val apiService : QuoteApi) : QuoteDataSource {

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
        //https://codinginfinite.com/kotlin-coroutine-call-adapter-retrofit/
    }
}

//*return if(response.isSuccessful) {
//    if(response.body() != null) {
//        Result.Success(response.body())
//    }
//    else {
//      Response.Error(EmptyResponseException())
//    }
//} else {
// Result.Error(EndpointRequestFailedException())
//}

//
//    try {
//        if (response.isSuccessful)
//            return Result.Success(response.body())
//        return Result.Error(IOException("Error occurred during fetching quotes!"))
//    } catch (e: Exception) {
//        Result.Error(IOException("Unable to fetch quotes!"))
//    }
//}