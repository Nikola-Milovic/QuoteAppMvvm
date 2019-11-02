package com.example.quoteappmvvm


import com.example.quoteappmvvm.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


open class QuoteApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
       return DaggerApplicationComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        // if (BuildConfig.DEBUG) Timber.plant(DebugTree())
    }
}