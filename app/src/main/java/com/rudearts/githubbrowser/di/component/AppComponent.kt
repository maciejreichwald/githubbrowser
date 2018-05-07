package com.rudearts.githubbrowser.di.component

import com.rudearts.githubbrowser.di.module.AppModule
import com.rudearts.githubbrowser.di.module.LocalModule
import com.rudearts.githubbrowser.di.module.RemoteModule
import com.rudearts.githubbrowser.di.module.ViewModelModule
import com.rudearts.githubbrowser.ui.location.DetailsActivity
import com.rudearts.githubbrowser.ui.main.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class), (RemoteModule::class), (LocalModule::class), (ViewModelModule::class)])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: DetailsActivity)
}