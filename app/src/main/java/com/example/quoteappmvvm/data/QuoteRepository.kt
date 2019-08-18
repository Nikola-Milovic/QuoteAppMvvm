package com.example.quoteappmvvm.data

import com.example.quoteappmvvm.data.db.entity.BaseRepository
import com.example.quoteappmvvm.data.network.model.Quote
import com.example.quoteappmvvm.data.network.model.QuoteApi

class QuoteRepository(private val api : QuoteApi) : BaseRepository() {

    suspend fun getQuotes()  : MutableList<Quote>? {

        //safeApiCall is defined in BaseRepository.kt (https://gist.github.com/navi25/67176730f5595b3f1fb5095062a92f15)
        val quoteResponse = safeApiCall(
            call = {api.getQuotes().await()},
            errorMessage = "Error Fetching Quotes"
        )

        return quoteResponse?.toMutableList()

    }

}