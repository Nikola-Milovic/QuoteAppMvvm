package com.example.quoteappmvvm.ui.quotes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quoteappmvvm.constants.apiState
import com.example.quoteappmvvm.data.QuoteRepository
import com.example.quoteappmvvm.data.Result
import com.example.quoteappmvvm.data.model.Quote
import com.example.quoteappmvvm.data.succeeded
import kotlinx.coroutines.launch
import javax.inject.Inject

class QuotesViewModel @Inject constructor(
    private val quoteRepository: QuoteRepository
) : ViewModel() {

    private val _items = MutableLiveData<List<Quote>>().apply { value = emptyList() }
    val items: LiveData<List<Quote>> = _items

    //Used for displaying the current quote
    private var _currentItem = MutableLiveData<Quote>()
    val currentItem: LiveData<Quote>
        get() = _currentItem


    private var _state = MutableLiveData<apiState>()
    val state: LiveData<apiState>
        get() = _state

    private var _currentQuote = MutableLiveData<Int>()
    val currentQuote: LiveData<Int>
        get() = _currentQuote


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
                    Log.d("TAG", "Doesnt have data")
                    fetchQuotes()
                }
            } catch (e: Exception) {
                Log.d("TAG", "Message " + e.message)
                _state.postValue(apiState.FAILURE) // SET THE INITIAL STATE AS FAILED
            }
        }

    }


    private fun fetchQuotes() {
        viewModelScope.launch {
            try {
                quoteRepository.fetchRemoteQuotes().let {
                    loadQuotes(quoteRepository.getQuotesFromLocalDataBase())
                }
            } catch (e: Exception) {
                Log.d("TAG", "Message " + e.message)
               _state.postValue(apiState.FAILURE) // SET THE INITIAL STATE AS FAILED
            }
        }
    }


    private fun loadQuotes(quoteResult: Result<List<Quote>>) {
        viewModelScope.launch {
            try {
//                val quoteResult = quoteRepository.getQuotesFromLocalDataBase()
                if (quoteResult is Result.Success) {
                    _state.postValue(apiState.SUCCESS)  // SET THE INITIAL STATE AS SUCCESS
                    val quotes = quoteResult.data
                    _items.postValue(quotes)
                    _currentQuote.value = getRandomNumber(items.value!!.size)

                } else {
                    _state.postValue(apiState.FAILURE) // SET THE INITIAL STATE AS FAILED
                    _items.postValue(null)
                }
            } catch (e: Exception) {
                _state.postValue(apiState.FAILURE) // SET THE INITIAL STATE AS FAILED
                _items.postValue(null)
            }
        }
    }

    fun setStateTest() {

        Log.d("TAG", state.value.toString())

//        if (_state.value == apiState.SUCCESS) {
//            _state.value = apiState.FAILURE
//        } else {
//            _state.value = apiState.SUCCESS
//        }
    }

    fun selectNewQuote() {
        if (_state.value == apiState.SUCCESS) {
            _currentQuote.value = getRandomNumber(items.value!!.size)
        }
    }

    private fun getRandomNumber(size: Int): Int {
        return (0 until size+1).random()
    }

}
