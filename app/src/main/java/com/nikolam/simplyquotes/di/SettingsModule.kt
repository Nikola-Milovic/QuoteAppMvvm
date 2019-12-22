package com.nikolam.simplyquotes.di

import androidx.lifecycle.ViewModel
import com.nikolam.simplyquotes.ui.settings.SettingsFragment
import com.nikolam.simplyquotes.ui.settings.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class SettingsModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun SettingsFragment(): SettingsFragment


    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindViewModel(viewmodel: SettingsViewModel): ViewModel
}
