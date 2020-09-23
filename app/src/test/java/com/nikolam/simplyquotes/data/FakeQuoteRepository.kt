package com.nikolam.simplyquotes.data

import com.nikolam.simplyquotes.data.model.Quote

class FakeQuoteRepository() : QuoteRepository{
    override suspend fun getQuotesFromLocalDataBase(): Result<List<Quote>> {
        return Result.Success(listOf(Quote("Test Author", "Test Quote Text", id = 5, isFavorite = false)))
    }

    override suspend fun fetchRemoteQuotes() {
        TODO("Not yet implemented")
    }

    override suspend fun getFavoriteQuotes(): Result<List<Quote>> {
        TODO("Not yet implemented")
    }

    override suspend fun favoriteAQuote(quoteID: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun unfavoriteAQuote(quoteID: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllFavoriteQuotes() {
        TODO("Not yet implemented")
    }

}