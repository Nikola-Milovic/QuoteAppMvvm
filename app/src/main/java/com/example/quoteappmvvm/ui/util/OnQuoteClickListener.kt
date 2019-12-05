package com.example.quoteappmvvm.ui.util

import com.example.quoteappmvvm.data.model.Quote

interface OnQuoteClickListener {
    fun quoteClicked(quote: Quote)
}