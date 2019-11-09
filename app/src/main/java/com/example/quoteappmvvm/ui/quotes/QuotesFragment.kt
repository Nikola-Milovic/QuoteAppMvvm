package com.example.quoteappmvvm.ui.quotes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.quoteappmvvm.databinding.QuotesFragmentBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class QuotesFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<QuotesViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: QuotesFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = QuotesFragmentBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        viewDataBinding.setLifecycleOwner(this)

        viewDataBinding.buttonReloadQuote.setOnClickListener {
            // viewModel.setStateTest()
            viewModel.selectNewQuote()
        }

        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}
