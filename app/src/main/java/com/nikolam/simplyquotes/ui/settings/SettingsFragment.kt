package com.nikolam.simplyquotes.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.nikolam.simplyquotes.R
import com.nikolam.simplyquotes.databinding.FragmentSettingsBinding
//import com.nikolam.simplyquotes.ui.util.interstatialID
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SettingsFragment : DaggerFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<SettingsViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: FragmentSettingsBinding

    private lateinit var mInterstitialAd: InterstitialAd


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentSettingsBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }


        setupOnClickListeners()
        setupAnimations()

        viewDataBinding.lifecycleOwner = this

        return viewDataBinding.root
    }

    override fun onStart() {
        super.onStart()
        setUpAds() // Load the advertisement beforehand
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun setupOnClickListeners() { // Setup Buttons onClicks
        viewDataBinding.imageButtonAdvertisement.setOnClickListener {
            try {
                if (mInterstitialAd.isLoaded) {
                    mInterstitialAd.show()
                } else {
                    Toast.makeText(
                        context,
                        "Please try again in a couple of seconds, the advertisement hasn't loaded yet",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.d("TAG", e.message)
                Toast.makeText(
                    context,
                    "Please try again in a couple of seconds, there was an error",
                    Toast.LENGTH_SHORT
                ).show()


            }
        }

        viewDataBinding.imageButtonContact.setOnClickListener {
            sendEmail()
        }

        viewDataBinding.imageButtonPaypal.setOnClickListener {
            openPaypal()
        }
    }

    fun setupAnimations() { // Setup animation of Views when they are appearing onViewCreated, just for looks
        lifecycleScope.launch {
            withContext(Dispatchers.Default) {
                val btt = AnimationUtils.loadAnimation(context, R.anim.btt)
                val ftv = AnimationUtils.loadAnimation(context, R.anim.ftv)

                viewDataBinding.textViewAbout.startAnimation(ftv)
                viewDataBinding.textViewAboutHeader.startAnimation(ftv)

                viewDataBinding.textViewPaypal.startAnimation(btt)
                viewDataBinding.textViewContact.startAnimation(btt)
                viewDataBinding.textViewAdvertisement.startAnimation(btt)
                viewDataBinding.imageButtonPaypal.startAnimation(btt)
                viewDataBinding.imageButtonContact.startAnimation(btt)
                viewDataBinding.imageButtonAdvertisement.startAnimation(btt)
            }
        }
    }


    fun setUpAds() { // Setup Ads after a delay so it doesn't slow down the Main Thread and delay creation of the Fragment
        lifecycleScope.launch {
            viewDataBinding.imageButtonAdvertisement.isClickable = false
            delay(1200)
            withContext(Dispatchers.Default) {
                viewDataBinding.imageButtonAdvertisement.isClickable = true
                MobileAds.initialize(context) {}
                mInterstitialAd = InterstitialAd(context)
               // mInterstitialAd.adUnitId = interstatialID
                withContext(Dispatchers.Main) {
                    mInterstitialAd.loadAd(AdRequest.Builder().build())
                    mInterstitialAd.adListener = object : AdListener() {
                        override fun onAdClosed() {
                            mInterstitialAd.loadAd(AdRequest.Builder().build())
                        }
                    }
                }
            }
        }
    }


    fun sendEmail() { // Opens up Gmail and starts a new E-mail to my E-mail address
        val email = Array(1) { "nikolamilovic2001@gmail.com" }
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, email)
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }

    fun openPaypal() { // Opens a paypal.me link, maybe someone feels generous
        val uris = Uri.parse("https://paypal.me/nikolamilovic176?locale.x=en_US")
        val intents = Intent(Intent.ACTION_VIEW, uris)
        val b = Bundle()
        b.putBoolean("new_window", true)
        intents.putExtras(b)
        requireContext().startActivity(intents)
    }


}