package com.example.quoteappmvvm.di

import androidx.lifecycle.ViewModel
import com.example.quoteappmvvm.ui.favorite.FavoriteQuotesFragment
import com.example.quoteappmvvm.ui.favorite.FavoriteQuotesViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class FavoriteQuotesModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun FavoriteQuotesFragment(): FavoriteQuotesFragment


    @Binds
    @IntoMap
    @ViewModelKey(FavoriteQuotesViewModel::class)
    abstract fun bindViewModel(viewmodel: FavoriteQuotesViewModel): ViewModel
}
