package com.example.quoteappmvvm.ui.util

import android.view.View
import android.widget.PopupMenu
import com.example.quoteappmvvm.data.model.Quote

interface OnQuoteClickListener : PopupMenu.OnMenuItemClickListener {
    fun quoteClicked(quote: Quote, view: View)
}