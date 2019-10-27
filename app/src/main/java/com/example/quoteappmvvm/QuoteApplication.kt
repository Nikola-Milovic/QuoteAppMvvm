package com.example.quoteappmvvm

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

open class QuoteApplication{
//    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
//
//        return DaggerApplicationComponent.factory().create(applicationContext)
//    }

//    override fun onCreate() {
//        super.onCreate()
//       // if (BuildConfig.DEBUG) Timber.plant(DebugTree())
//    }
}

/*open class QuoteApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {

        return DaggerApplicationComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
       // if (BuildConfig.DEBUG) Timber.plant(DebugTree())
    }
}*/