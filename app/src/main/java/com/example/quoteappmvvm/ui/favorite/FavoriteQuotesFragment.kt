package com.example.quoteappmvvm.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quoteappmvvm.data.model.Quote
import com.example.quoteappmvvm.databinding.FavoriteQuotesFragmentBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class FavoriteQuotesFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<FavoriteQuotesViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: FavoriteQuotesFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FavoriteQuotesFragmentBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        viewDataBinding.setLifecycleOwner(this)


        val mNicolasCageMovies = listOf(
            Quote("Raising Arizona", "bla"),
            Quote("Vampire's Kiss", "bla"),
            Quote("Con Air", "bla"),
            Quote("Gone in 60 Seconds", "bla")
        )

        viewDataBinding.quotesList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = FavoriteQuotesAdapter(mNicolasCageMovies)
        }
        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}
