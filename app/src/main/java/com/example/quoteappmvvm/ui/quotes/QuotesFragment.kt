package com.example.quoteappmvvm.ui.quotes

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.quoteappmvvm.R
import com.example.quoteappmvvm.databinding.QuotesFragmentBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class QuotesFragment : DaggerFragment() {

    companion object {
        private const val sharedPreferencesFile = "com.mypackagename.applicationname.preferences"
        private const val IS_FIRST_RUN = "isFirstRun"
    }

    private lateinit var sharedPreferences: SharedPreferences



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
            val anim = AnimationUtils.loadAnimation(context, R.anim.spin_anim)
            it.startAnimation(anim)
            viewModel.selectNewQuote()
        }

        viewDataBinding.favoriteAQuote.setOnClickListener {
            val anim = AnimationUtils.loadAnimation(context, R.anim.grow_anim)
            it.startAnimation(anim)
            viewModel.favoriteAQuote()
        }

        setHasOptionsMenu(true)

        setupAnim()

        sharedPreferences =
            requireActivity().getSharedPreferences(sharedPreferencesFile, Context.MODE_PRIVATE)

        if (sharedPreferences.getBoolean(IS_FIRST_RUN, true)) {
            Toast.makeText(context, "FIRST RUN", Toast.LENGTH_LONG).show()
            with(sharedPreferences.edit()) {
                putBoolean(IS_FIRST_RUN, false)
                apply()
            }
        }


        return viewDataBinding.root
    }

    fun setupAnim() {
        val btt = AnimationUtils.loadAnimation(context, R.anim.btt)
        val ftv = AnimationUtils.loadAnimation(context, R.anim.ftv)

        viewDataBinding.buttonReloadQuote.startAnimation(btt)
        viewDataBinding.favoriteAQuote.startAnimation(btt)
        viewDataBinding.textViewQuoteAuthor.startAnimation(ftv)
        viewDataBinding.textViewQuoteText.startAnimation(ftv)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}
