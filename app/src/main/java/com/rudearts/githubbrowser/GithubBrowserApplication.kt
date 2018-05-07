package com.rudearts.githubbrowser

import android.app.Application
import com.rudearts.githubbrowser.di.component.AppComponent
import com.rudearts.githubbrowser.di.component.DaggerAppComponent
import com.rudearts.githubbrowser.di.module.AppModule

class GithubBrowserApplication : Application() {

    lateinit var appComponent: AppComponent private set

    override fun onCreate() {
        super.onCreate()

        initInjector()
    }

    private fun initInjector() {
        appComponent = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }
}