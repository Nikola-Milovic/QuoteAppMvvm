package com.nikolam.simplyquotes.ui

import androidx.lifecycle.MutableLiveData
import com.nikolam.simplyquotes.data.FakeQuoteRepository
import com.nikolam.simplyquotes.data.model.Quote
import com.nikolam.simplyquotes.ui.quotes.QuotesViewModel
import com.nikolam.simplyquotes.utils.getOrAwaitValue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(manifest=Config.NONE)
class TeamViewModelTest {
    private lateinit var viewModel : QuotesViewModel

    private val repository = FakeQuoteRepository()

    @Before
    fun setup(){
        viewModel = QuotesViewModel(repository)
    }

    @Test
    fun `when created viewmodel, will select quote`(){
        val quote = viewModel.currentQuote.getOrAwaitValue()

        assert(!quote.isFavorite)
        assert(quote.quoteAuthor == "Test Author")
        assert(quote.quoteText == "Test Quote Text")
    }


}
