package com.rudearts.githubbrowser.di.module

import android.content.Context
import com.rudearts.githubbrowser.GithubBrowserApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val application: GithubBrowserApplication) {

    @Provides
    @Singleton
    fun provideApplication(): GithubBrowserApplication = application

    @Provides
    fun provideContext(): Context = application
}
