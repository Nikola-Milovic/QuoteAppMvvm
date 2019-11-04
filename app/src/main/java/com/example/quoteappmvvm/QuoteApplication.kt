package com.example.quoteappmvvm


import android.annotation.SuppressLint
import com.example.quoteappmvvm.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


@SuppressLint("Registered")
open class QuoteApplication : DaggerApplication(), HasAndroidInjector {


    @Inject
    lateinit var androidInjector : DispatchingAndroidInjector<Any>

    override  fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
       return DaggerApplicationComponent.factory().create(this)
    }



    override fun onCreate() {
        super.onCreate()
        // if (BuildConfig.DEBUG) Timber.plant(DebugTree())
    }
}