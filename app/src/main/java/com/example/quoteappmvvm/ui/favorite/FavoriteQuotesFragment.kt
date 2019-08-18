package com.example.quoteappmvvm.ui.favorite

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.quoteappmvvm.R

class FavoriteQuotesFragment : Fragment() {

    companion object {
        fun newInstance() = FavoriteQuotesFragment()
    }

    private lateinit var viewModel: FavoriteQuotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorite_quotes_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FavoriteQuotesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
