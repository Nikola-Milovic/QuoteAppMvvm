package com.nikolam.simplyquotes.ui.favorite

import android.content.ClipData
import android.content.ClipboardManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikolam.simplyquotes.data.QuoteRepository
import com.nikolam.simplyquotes.data.Result.Success
import com.nikolam.simplyquotes.data.model.Quote
import com.nikolam.simplyquotes.data.succeeded
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteQuotesViewModel @Inject constructor(
    private val quoteRepository: QuoteRepository
) : ViewModel() {

    private var _favoritequoteList = MutableLiveData<List<Quote>>()
    val favoritequoteList: LiveData<List<Quote>>
        get() = _favoritequoteList

    private lateinit var quotesList: ArrayList<Quote>

    private var _favoriteQuotesEmptyCheck = MutableLiveData<Boolean>()
    val favoriteQuotesEmptyCheck: LiveData<Boolean>
        get() = _favoriteQuotesEmptyCheck



    init {
        getQuotes()

    }

    private fun getQuotes() {
        viewModelScope.launch {
            _favoriteQuotesEmptyCheck.postValue(true)
            try {
                val quotes = quoteRepository.getFavoriteQuotes()
                if (quotes.succeeded && quotes is Success && quotes.data.isNotEmpty()) {
                    quotesList = ArrayList(quotes.data)
                    _favoritequoteList.postValue(quotesList)
                    _favoriteQuotesEmptyCheck.postValue(false)
                }
            } catch (e: Exception) {
                Log.d("TAG", e.message.toString())
            }
        }
    }

    suspend fun deleteFavoriteQuote(id: Int, quote: Quote) {
        viewModelScope.launch {
            quoteRepository.unfavoriteAQuote(id)
            if (::quotesList.isInitialized) {
                quotesList.remove(quote)
                _favoritequoteList.postValue(quotesList)
                checkForEmpty()
            }
        }
    }

    fun checkForEmpty() {
        viewModelScope.launch {
            if (quotesList.isEmpty()) {
                _favoriteQuotesEmptyCheck.postValue(true)
            } else {
                _favoriteQuotesEmptyCheck.postValue(false)
            }
        }
    }


    suspend fun deleteAllFavoriteQuotes() {
        viewModelScope.launch {
            quoteRepository.deleteAllFavoriteQuotes()
            checkForEmpty()
        }

    }

    fun copyText(quote: Quote, clipboardManager: ClipboardManager) {
        val clip: ClipData =
            ClipData.newPlainText("quoteCopied", quote.quoteText + "\n" + " - " + quote.quoteAuthor)
        clipboardManager.setPrimaryClip(clip)
    }
}


