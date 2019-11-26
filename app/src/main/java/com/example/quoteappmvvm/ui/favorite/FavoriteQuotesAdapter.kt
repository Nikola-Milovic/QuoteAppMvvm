package com.example.quoteappmvvm.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quoteappmvvm.R
import com.example.quoteappmvvm.data.model.Quote


class FavoriteQuotesAdapter(private val list: List<Quote>) :
    RecyclerView.Adapter<FavoriteQuoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteQuoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FavoriteQuoteViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: FavoriteQuoteViewHolder, position: Int) {
        val quote: Quote = list[position]
        holder.bind(quote)
    }

    override fun getItemCount(): Int = list.size

}


class FavoriteQuoteViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.favorite_quote_item, parent, false)) {
    private var quoteAuthorTextView: TextView? = null
    private var quoteTextTextView: TextView? = null


    init {
        quoteAuthorTextView = itemView.findViewById(R.id.textView_favorite_author)
        quoteTextTextView = itemView.findViewById(R.id.textView_favorite_text)
    }

    fun bind(quote: Quote) {
        quoteAuthorTextView?.text = quote.quoteAuthor
        quoteTextTextView?.text = quote.quoteText
    }

}