package com.example.quoteappmvvm.ui.quotes

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import com.example.quoteappmvvm.data.DefaultQuoteRepository
import com.example.quoteappmvvm.data.QuoteRepository
import com.example.quoteappmvvm.data.network.JsonNetworkService
import com.example.quoteappmvvm.data.model.Quote
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class QuotesViewModel @Inject constructor(
    private val repository: QuoteRepository
) : ViewModel() {

}
