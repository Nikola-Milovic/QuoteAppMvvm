package com.example.quoteappmvvm.ui.quotes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewModelScope
import com.example.quoteappmvvm.data.QuoteRepository
import com.example.quoteappmvvm.data.Result
import com.example.quoteappmvvm.data.model.Quote
import kotlinx.coroutines.*
import javax.inject.Inject

class QuotesViewModel @Inject constructor(
    private val quoteRepository: QuoteRepository
) : ViewModel() {

    private val _items = MutableLiveData<List<Quote>>().apply { value = emptyList() }
    val items: LiveData<List<Quote>> = _items

    init{
        loadQuotes()
    }

    fun loadQuotes() {
            viewModelScope.launch {
                val quoteResult = quoteRepository.getQuotes()

                if (quoteResult is Result.Success) {
                    val quotes = quoteResult.data
                    _items.value = quotes
                }
            }
    }
}
