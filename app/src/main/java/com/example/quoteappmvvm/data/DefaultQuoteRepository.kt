package com.example.quoteappmvvm.data

import com.example.quoteappmvvm.data.db.entity.BaseRepository
import com.example.quoteappmvvm.data.local.QuoteLocalDataSource
import com.example.quoteappmvvm.data.network.QuoteRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton



class DefaultQuoteRepository @Inject constructor(
    private val quoteRemoteDataSource : QuoteRemoteDataSource,
    private val quoteLocalDataSource: QuoteLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseRepository() {

}