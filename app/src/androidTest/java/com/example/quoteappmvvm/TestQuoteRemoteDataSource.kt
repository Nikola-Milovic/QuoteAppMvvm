package com.example.quoteappmvvm;

import com.example.quoteappmvvm.data.Result;
import com.example.quoteappmvvm.data.model.Quote;
import com.example.quoteappmvvm.data.model.QuoteApi;
import javax.inject.Inject;

open class TestQuoteRemoteDataSource @Inject constructor(
    private val apiService: QuoteApi
) {
    open suspend fun getQuotes(): Result<List<Quote>> {
        TODO()
    }
}

