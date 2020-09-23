package com.nikolam.simplyquotes.ui.quotes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikolam.simplyquotes.constants.apiState
import com.nikolam.simplyquotes.data.QuoteRepository
import com.nikolam.simplyquotes.data.Result
import com.nikolam.simplyquotes.data.model.Quote
import com.nikolam.simplyquotes.data.succeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuotesViewModel @Inject constructor(
    private val quoteRepository: QuoteRepository
) : ViewModel() {

    private lateinit var quotesList: List<Quote>
    //Used for displaying the current quote
    private var _currentQuote = MutableLiveData<Quote>()
    val currentQuote: LiveData<Quote>
        get() = _currentQuote

    lateinit var quote: Quote

    private var _state = MutableLiveData<apiState>()
    val state: LiveData<apiState>
        get() = _state

    init {
        startTheApp()
    }


    private fun startTheApp() {
        viewModelScope.launch {
            _state.postValue(apiState.LOADING)
            try {
                val result = quoteRepository.getQuotesFromLocalDataBase()
                if (result.succeeded) {
                    Log.d("TAG", "Already has data")
                    loadQuotes(result)
                } else {
                    Log.d("TAG", "Doesn't have data")
                    fetchQuotes()
                }
            } catch (e: Exception) {
                Log.d("TAG", "Message is start app " + e.localizedMessage)
                _state.postValue(apiState.FAILURE) // SET THE INITIAL STATE AS FAILED
            }
        }

    }

    fun favoriteAQuote() {
        viewModelScope.launch {
            if (state.value == apiState.SUCCESS && ::quote.isInitialized) {
                quoteRepository.favoriteAQuote(quote.id)
            }
        }
    }

    private fun fetchQuotes() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    quoteRepository.fetchRemoteQuotes().let {
                    loadQuotes(quoteRepository.getQuotesFromLocalDataBase())
                    }
                }

            } catch (e: Exception) {
                Log.d("TAG", "Message fetch + " + e.localizedMessage)
               _state.postValue(apiState.FAILURE) // SET THE INITIAL STATE AS FAILED
            }
        }
    }

    private fun loadQuotes(quoteResult: Result<List<Quote>>) {
        viewModelScope.launch {
            try {
                if (quoteResult is Result.Success) {
                    _state.postValue(apiState.SUCCESS)  // SET THE INITIAL STATE AS SUCCESS
                    quotesList = quoteResult.data
                    val pos = getRandomNumber(quotesList.size)
                    quote = quotesList[pos]
                    _currentQuote.postValue(quotesList[pos])

                } else {
                    _state.postValue(apiState.FAILURE) // SET THE INITIAL STATE AS FAILED
                    _currentQuote.postValue(null)
                }
            } catch (e: Exception) {
                _state.postValue(apiState.FAILURE) // SET THE INITIAL STATE AS FAILED
                _currentQuote.postValue(null)
            }
        }
    }

    fun selectNewQuote() {
        if (_state.value == apiState.SUCCESS) {
            val pos = getRandomNumber(quotesList.size)
            quote = quotesList[pos]
            _currentQuote.value = quotesList[pos]
        }
    }

    private fun getRandomNumber(size: Int): Int {
        return (0 until size+1).random()
    }

}
