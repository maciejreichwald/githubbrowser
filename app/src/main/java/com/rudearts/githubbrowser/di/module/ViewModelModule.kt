package com.rudearts.githubbrowser.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.rudearts.githubbrowser.di.ViewModelFactory
import com.rudearts.githubbrowser.ui.location.DetailsViewModel
import com.rudearts.githubbrowser.ui.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelFactory.ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelFactory.ViewModelKey(DetailsViewModel::class)
    internal abstract fun binLocationViewModel(viewModel: DetailsViewModel): ViewModel

}
