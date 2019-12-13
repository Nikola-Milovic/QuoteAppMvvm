package com.example.quoteappmvvm.ui.quotes

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.quoteappmvvm.R
import com.example.quoteappmvvm.databinding.QuotesFragmentBinding
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.createBalloon
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class QuotesFragment : DaggerFragment() {

    companion object {
        private const val sharedPreferencesFile = "com.mypackagename.applicationname.preferences"
        private const val IS_FIRST_RUN_QUOTES = "isFirstRunQuotes"
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

//        sharedPreferences =
//            requireActivity().getSharedPreferences(sharedPreferencesFile, Context.MODE_PRIVATE)
//
//        if (sharedPreferences.getBoolean(IS_FIRST_RUN_QUOTES, true)) {
//            Toast.makeText(context, "FIRST RUN", Toast.LENGTH_LONG).show()
//            with(sharedPreferences.edit()) {
//                putBoolean(IS_FIRST_RUN_QUOTES, false)
//                apply()
//            }
//        }

        firstRun()


        return viewDataBinding.root
    }

    fun firstRun() {
        lifecycleScope.launch {
            delay(1000)
            val balloon3 = createBalloon(requireContext()) {
                setArrowSize(10)
                setWidthRatio(0.8f)
                setHeight(50)
                setArrowPosition(0.25f)
                setArrowOrientation(ArrowOrientation.BOTTOM)
                setCornerRadius(4f)
                setAlpha(0.9f)
                setDismissWhenClicked(true)
                setDismissWhenTouchOutside(true)
                setText(" Click here to view your Favorited Quotes")
                setTextColorResource(R.color.colorAccent)
                setBackgroundColorResource(R.color.colorPrimary)
                setBalloonAnimation(BalloonAnimation.FADE)
                setLifecycleOwner(this@QuotesFragment)
            }


            val balloon2 = createBalloon(requireContext()) {
                setArrowSize(10)
                setWidthRatio(0.8f)
                setHeight(50)
                setArrowPosition(0.25f)
                setArrowOrientation(ArrowOrientation.TOP)
                setCornerRadius(4f)
                setAlpha(0.9f)
                setDismissWhenClicked(true)
                setDismissWhenTouchOutside(true)
                setText("Favorite a quote and save it for later viewing!")
                setOnBalloonDismissListener {
                    balloon3.showAlignBottom(viewDataBinding.buttonReloadQuote, 0, 150)
                }
                setTextColorResource(R.color.colorAccent)
                setBackgroundColorResource(R.color.colorPrimary)
                setBalloonAnimation(BalloonAnimation.FADE)
                setLifecycleOwner(this@QuotesFragment)
            }


            val balloon1 = createBalloon(requireContext()) {
                setArrowSize(10)
                setWidthRatio(0.8f)
                setHeight(50)
                setArrowPosition(0.5f)
                setArrowOrientation(ArrowOrientation.TOP)
                setCornerRadius(4f)
                setAlpha(0.9f)
                setDismissWhenClicked(true)
                setDismissWhenTouchOutside(true)
                setOnBalloonDismissListener {
                    balloon2.showAlignBottom(viewDataBinding.favoriteAQuote, 0, -50)
                }
                setText("Refresh and see a brand new quote!")
                setTextColorResource(R.color.colorAccent)
                setBackgroundColorResource(R.color.colorPrimary)
                setBalloonAnimation(BalloonAnimation.FADE)
                setLifecycleOwner(this@QuotesFragment)
            }

            balloon1.showAlignBottom(viewDataBinding.buttonReloadQuote, 0, -50)

        }
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
