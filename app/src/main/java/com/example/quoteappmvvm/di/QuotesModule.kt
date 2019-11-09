package com.example.quoteappmvvm.di

import androidx.lifecycle.ViewModel
import com.example.quoteappmvvm.ui.quotes.QuotesFragment
import com.example.quoteappmvvm.ui.quotes.QuotesViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class QuotesModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun QuotesFragment(): QuotesFragment

    @Binds
    @IntoMap
    @ViewModelKey(QuotesViewModel::class)
    abstract fun bindViewModel(viewmodel: QuotesViewModel): ViewModel
}
