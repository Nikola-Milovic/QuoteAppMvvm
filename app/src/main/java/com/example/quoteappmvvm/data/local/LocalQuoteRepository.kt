package com.example.quoteappmvvm.data.local

import com.example.quoteappmvvm.data.db.entity.BaseRepository
import com.example.quoteappmvvm.data.network.JsonNetworkService
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalQuoteRepository  (
): BaseRepository(){

    init{
        //initGetQuotesAndStoreThem()
    }

}