package com.rudearts.githubbrowser.di.module

import com.rudearts.githubbrowser.BuildConfig
import com.rudearts.githubbrowser.data.remote.RemoteCalls
import com.rudearts.githubbrowser.data.remote.RestApi
import com.rudearts.githubbrowser.data.util.RestMapper
import com.rudearts.githubbrowser.domain.remote.RemoteCallable
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RemoteModule {

    private val BASE_URL = "https://api.github.com/"

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
        val clientBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(httpLoggingInterceptor)
        }
        return clientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    @Provides
    fun provideRestApi(retrofit: Retrofit) = retrofit.create(RestApi::class.java) as RestApi

    @Provides
    fun provideRemoteCallable(restApi: RestApi,
                              mapper: RestMapper) = RemoteCalls(restApi, mapper) as RemoteCallable
}