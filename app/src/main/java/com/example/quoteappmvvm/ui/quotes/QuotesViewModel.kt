package com.example.quoteappmvvm.ui.quotes

import androidx.core.util.rangeTo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import com.example.quoteappmvvm.data.QuoteRepository
import com.example.quoteappmvvm.data.network.JsonNetworkService
import com.example.quoteappmvvm.data.network.model.Quote
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class QuotesViewModel : ViewModel() {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository : QuoteRepository = QuoteRepository(JsonNetworkService.quoteApi)


    val quotesLiveData = MutableLiveData<MutableList<Quote>>()

    //Mine
    var currentQuote : Quote? = Quote("null" , "null")
    var currentQuoteLiveData : MutableLiveData<Quote> = MutableLiveData<Quote>()
    var quotes : MutableList<Quote>? = null

    fun fetchQuotes(){
        scope.launch {
            quotes = repository.getQuotes()
            quotesLiveData.postValue(quotes)
            newCurrentQuote()
        }
    }

    fun newCurrentQuote(){
        scope.launch {
           val pos = getRandomNumber()
            currentQuote  = quotes?.get(pos)
            currentQuoteLiveData.postValue(currentQuote)
        }
    }

    fun getRandomNumber() : Int{
        val rnds  = (1..5000).random()
        return rnds
    }


    fun cancelAllRequests() = coroutineContext.cancel()
}
