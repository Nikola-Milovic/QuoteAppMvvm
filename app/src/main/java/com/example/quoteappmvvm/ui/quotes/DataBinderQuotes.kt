package com.example.quoteappmvvm.ui.quotes

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.quoteappmvvm.constants.apiState
import com.example.quoteappmvvm.constants.apiState.*

//@BindingAdapter("statusImage")
//fun setVisibility(imageView: ImageView, apiState: apiState) {
//    when(apiState){
//        FAILURE ->{imageView.visibility = View.VISIBLE}
//        SUCCESS ->{imageView.visibility = View.INVISIBLE}
//        LOADING ->{imageView.visibility = View.INVISIBLE}
//    }
//}

@BindingAdapter("viewNoConnectionVisibility")
fun View.setNoConnectionVisibility(apiState: apiState?) {
    this.visibility = when (apiState) {
        FAILURE -> View.VISIBLE
        SUCCESS -> View.INVISIBLE
        LOADING -> View.INVISIBLE
        else -> View.INVISIBLE
    }
}

@BindingAdapter("viewConnectionVisibility")
fun View.setConnectionVisibility(apiState: apiState?) {
    this.visibility = when (apiState) {
        FAILURE -> View.INVISIBLE
        SUCCESS -> View.VISIBLE
        LOADING -> View.INVISIBLE
        else -> View.INVISIBLE
    }
}


@BindingAdapter("viewLoadingVisibility")
fun View.setLoadingVisibility(apiState: apiState?) {
    this.visibility = when (apiState) {
        FAILURE -> View.INVISIBLE
        SUCCESS -> View.INVISIBLE
        LOADING -> View.VISIBLE
        else -> View.VISIBLE
    }
}

@BindingAdapter("noAuthorText")
fun TextView.noAuthorText(item: String?) {
    item?.let {
        text = if(item.isNullOrBlank()) {
            "Unknown Author"
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
