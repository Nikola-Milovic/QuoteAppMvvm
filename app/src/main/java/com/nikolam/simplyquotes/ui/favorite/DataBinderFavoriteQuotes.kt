package com.nikolam.simplyquotes.ui.favorite

import android.widget.TextView
import androidx.databinding.BindingAdapter


// Changing the empty author in the Text View

@BindingAdapter("noAuthorText")
fun TextView.noAuthorText(item: String?) {
    item?.let {
        text = if(item.isNullOrBlank()) {
            "- Unknown Author"
        } else{
            "- $item"
        }
    }
}

