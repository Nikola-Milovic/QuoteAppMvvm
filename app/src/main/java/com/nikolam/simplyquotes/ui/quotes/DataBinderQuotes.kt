package com.nikolam.simplyquotes.ui.quotes

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.nikolam.simplyquotes.constants.apiState
import com.nikolam.simplyquotes.constants.apiState.*


// Show a View when there is no Internet connection
@BindingAdapter("viewNoConnectionVisibility")
fun View.setNoConnectionVisibility(apiState: apiState?) {
    this.visibility = when (apiState) {
        FAILURE -> View.VISIBLE
        SUCCESS -> View.INVISIBLE
        LOADING -> View.INVISIBLE
        else -> View.INVISIBLE
    }
}

// Show a View when there is  Internet connection
@BindingAdapter("viewConnectionVisibility")
fun View.setConnectionVisibility(apiState: apiState?) {
    this.visibility = when (apiState) {
        FAILURE -> View.INVISIBLE
        SUCCESS -> View.VISIBLE
        LOADING -> View.INVISIBLE
        else -> View.INVISIBLE
    }
}

// Show a View when the app is Loading
@BindingAdapter("viewLoadingVisibility")
fun View.setLoadingVisibility(apiState: apiState?) {
    this.visibility = when (apiState) {
        FAILURE -> View.INVISIBLE
        SUCCESS -> View.INVISIBLE
        LOADING -> View.VISIBLE
        else -> View.VISIBLE
    }
}

// Show Unknown Author when the Quote Author Text is empty
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


//@BindingAdapter("emptyAuthor")
//fun TextView.emptyAuthor(string: String){
//    if(string.isNullOrBlank()){
//        TextView.text = "Unknown Author"
//    }
//}
