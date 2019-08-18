package com.example.quoteappmvvm.data.network.model

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

// Data Model for TMDB Movie item
data class Quote(
   val quoteAuthor : String,
   val quoteText : String
)

// Data Model for the Response returned from the TMDB Api
data class QuoteResponse(
    val results : List<Quote>
)

//A retrofit Network Interface for the Api
interface QuoteApi{
    @GET("quotes.json")
    //fun getQuotes(): Deferred<Response<QuoteResponse>>
    fun getQuotes(): Deferred<Response<List<Quote>>>
}