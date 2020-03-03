package com.nikolam.simplyquotes

import android.content.Context
import com.nikolam.simplyquotes.di.NetworkModule
import com.nikolam.simplyquotes.di.QuotesModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        TestApplicationModule::class,
        AndroidSupportInjectionModule::class,
        QuotesModule::class,
        NetworkModule::class
    ]
)
interface TestApplicationComponent : AndroidInjector<TestQuoteApplication> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): TestApplicationComponent
    }
}
