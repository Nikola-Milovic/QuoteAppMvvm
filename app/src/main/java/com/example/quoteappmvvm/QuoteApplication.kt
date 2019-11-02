package com.example.quoteappmvvm


import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


open class QuoteApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
     //  return DaggerApplicationComponent.factory().create(this)
        TODO()
    }

    override fun onCreate() {
        super.onCreate()
        // if (BuildConfig.DEBUG) Timber.plant(DebugTree())
    }
}