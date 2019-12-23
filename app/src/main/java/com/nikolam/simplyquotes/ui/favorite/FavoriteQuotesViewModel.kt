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

    private var _quoteList = MutableLiveData<List<Quote>>()
    val quoteList: LiveData<List<Quote>>
        get() = _quoteList

    init {
        getQuote()
    }

    private fun getQuote() {
        viewModelScope.launch {
            try {
                val quotes = quoteRepository.getFavoriteQuotes()
                if (quotes.succeeded && quotes is Success) {
                    _quoteList.postValue(quotes.data)
                }
            } catch (e: Exception) {
                Log.d("TAG", e.message.toString())
            }
        }
    }

    suspend fun deleteFavoriteQuote(id: Int) {
        viewModelScope.launch {
            quoteRepository.unfavoriteAQuote(id)
        }
    }

    suspend fun deleteAllFavoriteQuotes() {
        viewModelScope.launch {
            quoteRepository.deleteAllFavoriteQuotes()
        }

    }

    fun copyText(quote: Quote, clipboardManager: ClipboardManager) {
        val clip: ClipData =
            ClipData.newPlainText("quoteCopied", quote.quoteText + "\n" + " - " + quote.quoteAuthor)
        clipboardManager.setPrimaryClip(clip)
    }
}

