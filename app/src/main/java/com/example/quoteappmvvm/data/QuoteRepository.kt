package com.example.quoteappmvvm.data

import android.util.Log
import com.example.quoteappmvvm.data.db.entity.BaseRepository
import com.example.quoteappmvvm.data.network.model.Quote
import com.example.quoteappmvvm.data.network.model.QuoteApi
import java.lang.Exception

class QuoteRepository(private val api : QuoteApi) : BaseRepository() {
     var loadedQuotes : Boolean = false
    fun checkIfNeededToFetchQuotes() : Boolean {
        return loadedQuotes
    }


    suspend fun getQuotes()  : MutableList<Quote>? {

        //safeApiCall is defined in BaseRepository.kt (https://gist.github.com/navi25/67176730f5595b3f1fb5095062a92f15)
        try {
            val quoteResponse = safeApiCall(
                call = { api.getQuotes().await() },
                errorMessage = "Error Fetching Quotes"
            )
            loadedQuotes = true
            return quoteResponse?.toMutableList()
        } catch(e : Exception){
            loadedQuotes = false
            Log.d("TAG", "Nema neta")
        }

        val noInternet = mutableListOf<Quote>()
        noInternet.add(Quote("Dev", "No internet connection"))
        return noInternet
    }

}