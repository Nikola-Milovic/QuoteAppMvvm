package com.example.quoteappmvvm.ui.favorite

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quoteappmvvm.data.QuoteRepository
import com.example.quoteappmvvm.data.model.Quote
import com.example.quoteappmvvm.data.succeeded
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteQuotesViewModel @Inject constructor(
    private val quoteRepository: QuoteRepository
) : ViewModel() {

    lateinit var quotesListTest: List<Quote>

    init {
        getQuote()
    }


    private fun getQuote() {
        viewModelScope.launch {
            try {
                val quotes = quoteRepository.getQuotesFromLocalDataBase()
                if (quotes.succeeded) {
                    // quotesListTest = quoteResult.data
                }
            } catch (e: Exception) {
                Log.d("TAG", e.message.toString())
            }
        }
    }
}
