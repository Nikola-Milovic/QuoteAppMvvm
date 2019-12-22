package com.nikolam.simplyquotes;

import com.nikolam.simplyquotes.data.Result;
import com.nikolam.simplyquotes.data.model.Quote;
import com.nikolam.simplyquotes.data.model.QuoteApi;
import javax.inject.Inject;

open class TestQuoteRemoteDataSource @Inject constructor(
    private val apiService: QuoteApi
) {
    open suspend fun getQuotes(): Result<List<Quote>> {
        TODO()
    }
}

