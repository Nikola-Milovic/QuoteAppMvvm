package com.nikolam.simplyquotes.util

import android.view.View
import android.widget.PopupMenu
import com.nikolam.simplyquotes.data.model.Quote

interface OnQuoteClickListener : PopupMenu.OnMenuItemClickListener {
    fun quoteClicked(quote: Quote, view: View)
}