package com.example.quoteappmvvm.ui.favorite

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("noAuthorText")
fun TextView.noAuthorText(item: String?) {
    item?.let {
        text = if(item.isNullOrBlank()) {
            "Unknown Author"
        } else{
            item
        }
    }
}
