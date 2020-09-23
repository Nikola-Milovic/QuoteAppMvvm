package com.nikolam.simplyquotes.data.network

import com.nikolam.simplyquotes.data.model.Quote
import retrofit2.http.GET

//A retrofit Network Interface for the Api
interface QuoteApi {
    @GET("quotes.json")
    suspend fun getQuotes(): List<Quote>
}