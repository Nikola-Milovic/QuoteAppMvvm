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
    var currentQuoteLiveData : MutableLiveData<Quote> = MutableLiveData()
    var quotes = mutableListOf<Quote>()

    fun fetchQuotes(){
        scope.launch {
            quotes = repository.getQuotes()!!
            quotesLiveData.postValue(quotes)
            newCurrentQuote()
        }
    }

    fun newCurrentQuote(){
        scope.launch {
            if(!repository.loadedQuotes){
                fetchQuotes()
            }
           val pos = getRandomNumber(quotes.size)
            currentQuote  = quotes.get(pos)
            currentQuoteLiveData.postValue(currentQuote)
        }
    }

    fun getRandomNumber(size : Int) : Int{
        val pos  = (0..size - 1).random()
        return pos
    }


    fun cancelAllRequests() = coroutineContext.cancel()
}
