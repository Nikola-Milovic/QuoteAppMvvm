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
        fetchQuotes()
        loadQuotes()
    }


    fun fetchQuotes(){
        viewModelScope.launch {
            try{
                quoteRepository.fetchQuotes()
            } catch(e: Exception){
                Log.d("TAG", "Message " +  e.message)
                _state.value = apiState.FAILURE // SET THE INITIAL STATE AS FAILED
            }
        }
    }


    fun loadQuotes() {
        viewModelScope.launch {
            _state.value = apiState.LOADING // SET THE INITIAL STATE AS LOADING
            try {
                val quoteResult = quoteRepository.getQuotes()

                if (quoteResult is Result.Success) {
                    _state.value = apiState.SUCCESS // SET THE INITIAL STATE AS SUCCESS
                    val quotes = quoteResult.data
                    _items.value = quotes

                    _currentQuote.value = getRandomNumber(items.value!!.size)

                } else {
                    _state.value = apiState.FAILURE // SET THE INITIAL STATE AS FAILED
                    _items.value = null
                }
            } catch (e: Exception) {
                _state.value = apiState.FAILURE // SET THE INITIAL STATE AS FAILED
                _items.value = null
            }
        }
    }

    fun setStateTest() {

        Log.d("TAG", state.value.toString())

        if (_state.value == apiState.SUCCESS) {
            _state.value = apiState.FAILURE
        } else {
            _state.value = apiState.SUCCESS
        }
    }

    fun selectNewQuote() {
        if (_state.value == apiState.SUCCESS) {
            _currentQuote.value = getRandomNumber(items.value!!.size)
        }
    }

    fun getRandomNumber(size: Int): Int {
        return (0..size-1).random()
    }

}
