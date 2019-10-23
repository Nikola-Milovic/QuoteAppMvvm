package com.example.quoteappmvvm.ui.quotes

import android.util.Log
import androidx.core.util.rangeTo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import com.example.quoteappmvvm.data.QuoteRepository
import com.example.quoteappmvvm.data.network.JsonNetworkService
import com.example.quoteappmvvm.data.network.model.Quote
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext
import kotlin.math.log

class QuotesViewModel : ViewModel() {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository : QuoteRepository = QuoteRepository(JsonNetworkService.quoteApi)

    val quotesLiveData = MutableLiveData<MutableList<Quote>>()

    //Mine
    //var currentQuote : Quote = Quote("Dev" , "No internet connection")
    //var currentQuoteLiveData : MutableLiveData<Quote> = MutableLiveData()

    var quotes: List<Quote> = listOf()

    fun fetchQuotes(){
        scope.launch {
            quotes = repository.getQuotes() as List<Quote>
            quotesLiveData.postValue(quotes.toMutableList())
           // newCurrentQuote() // Popraviti ovo da ne bude novi quote svaki put kad se promeni activity
            if(repository.loadedQuotes){
                newCurrentQuote()
            }
        }
    }

    fun newCurrentQuote(){
        scope.launch {
            if (!repository.loadedQuotes) {
                tryToReloadQuotes()
            }
            //if(!quotes.isEmpty()) {
               // val pos = getRandomNumber(quotes.size)
//            Log.d("TAG", "Quotes2 " + quotes.toString() )
                //currentQuote = quotes[pos]
          //  }
          //  currentQuoteLiveData.postValue(currentQuote)
        }
    }

    fun getRandomNumber(size : Int) : Int{
        val pos  = (0..size-1).random()
        return pos
    }

    fun tryToReloadQuotes() {
        scope.launch {
            try {
                repeat(1000) { i ->
                    if (!repository.loadedQuotes) { // kada si offline nikad ne fetchuje i onda stalno proverava
                        fetchQuotes()
                    }
                    delay(300)
                }
            } catch (e: Exception) {
                Log.d("TAG", "Still offline")
            }
        }
    }

    fun cancelAllRequests() = coroutineContext.cancel()
}
