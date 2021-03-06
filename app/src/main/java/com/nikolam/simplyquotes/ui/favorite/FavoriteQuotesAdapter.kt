package com.nikolam.simplyquotes.ui.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nikolam.simplyquotes.R
import com.nikolam.simplyquotes.data.model.Quote
import com.nikolam.simplyquotes.util.OnQuoteClickListener


class FavoriteQuotesAdapter(
    private val context: Context,
    private val clickListener: OnQuoteClickListener
) :
    RecyclerView.Adapter<FavoriteQuotesAdapter.FavoriteQuoteViewHolder>() {

    var quoteList = arrayListOf<Quote>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteQuoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_quote_item,parent,false)
        return FavoriteQuoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteQuoteViewHolder, position: Int) {
        val quote: Quote = quoteList[position]
        holder.bind(quote)
    }

    override fun getItemCount(): Int = quoteList.size

    fun getQuoteAt(position: Int): Quote {
        return quoteList[position]
    }

    // Handle updates to the List without causing a lag
    fun updateQuotes(quotes: List<Quote>) {
        DiffUtil.calculateDiff(FavoriteQuoteDiffCallback(quotes, quoteList), false)
            .dispatchUpdatesTo(this)
        quoteList = ArrayList(quotes)
    }

    fun removeQuote(quotePos: Int) {
        quoteList.removeAt(quotePos)
        notifyItemRemoved(quotePos)
    }

    fun deleteAll() {
        quoteList.clear()
        notifyDataSetChanged()
    }

    inner class FavoriteQuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var quoteAuthorTextView: TextView? = null
        private var quoteTextTextView: TextView? = null

        init {
            quoteAuthorTextView = itemView.findViewById(R.id.textView_favorite_author)
            quoteTextTextView = itemView.findViewById(R.id.textView_favorite_text)
        }

        fun bind(quote: Quote) {
            if (quote.quoteAuthor.isNullOrBlank()) {
                quoteAuthorTextView?.text = "- Unknown Author"
            } else {
                quoteAuthorTextView?.text = "- " + quote.quoteAuthor
            }

            quoteTextTextView?.text = quote.quoteText

            itemView.setOnClickListener {
                clickListener.quoteClicked(quote, itemView)
            }
        }

    }
}

// DiffCallBack to help Recycle View handle updates and changes
class FavoriteQuoteDiffCallback(
    private val newQuotes: List<Quote>,
    private val oldQuotes: List<Quote>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldQuote = oldQuotes[oldItemPosition]
        val newQuote = newQuotes[newItemPosition]
        return oldQuote.id == newQuote.id
    }

    override fun getOldListSize(): Int = oldQuotes.size

    override fun getNewListSize(): Int = newQuotes.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldQuote = oldQuotes[oldItemPosition]
        val newQuote = newQuotes[newItemPosition]
        return oldQuote == newQuote
    }
}