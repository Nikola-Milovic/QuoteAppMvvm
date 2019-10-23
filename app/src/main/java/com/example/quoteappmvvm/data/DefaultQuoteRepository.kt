package com.example.quoteappmvvm.data

import com.example.quoteappmvvm.data.db.entity.BaseRepository
import com.example.quoteappmvvm.data.model.QuoteApi
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DefaultQuoteRepository @Inject constructor(
    private val api : QuoteApi
) : BaseRepository() {



}