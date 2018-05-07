package com.rudearts.githubbrowser.di.module

import com.rudearts.githubbrowser.data.local.UserStorage
import com.rudearts.githubbrowser.domain.remote.UserCache
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalModule {

    @Provides
    @Singleton
    fun providesUserCache() = UserStorage() as UserCache

}