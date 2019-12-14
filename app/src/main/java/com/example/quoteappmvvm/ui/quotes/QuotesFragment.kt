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


        enableNavigation()
        firstRun()


        return viewDataBinding.root
    }

    fun firstRun() {
        lifecycleScope.launch {
            disableNavigation()
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
                setOnBalloonDismissListener { enableNavigation() }
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

    fun disableNavigation() {
        lifecycleScope.launch {
            val view = requireActivity().findViewById<View>(R.id.favoriteQuotesFragment)
            val view2 = requireActivity().findViewById<View>(R.id.settingsFragment)
            view.isClickable = false
            view2.isClickable = false
        }
    }

    fun enableNavigation() {
        lifecycleScope.launch {
            val view = requireActivity().findViewById<View>(R.id.favoriteQuotesFragment)
            val view2 = requireActivity().findViewById<View>(R.id.settingsFragment)
            view.isClickable = true
            view2.isClickable = true
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}


// private fun hideSystemUI() {
// // Enables sticky immersive mode.
// // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE_STICKY.
// // Or for "regular immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE
// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
// requireActivity().window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
// // Set the content to appear under the system bars so that the
// // content doesn't resize when the system bars hide and show.
// or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
// or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
// or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
// // Hide the nav bar and status bar
// or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
// or View.SYSTEM_UI_FLAG_FULLSCREEN)
// }
// }
//
// // Shows the system bars by removing all the flags
// // except for the ones that make the content appear under the system bars.
// private fun showSystemUI() {
// requireActivity().window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
// or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
// or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
// }