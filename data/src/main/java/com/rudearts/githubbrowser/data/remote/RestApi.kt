package com.rudearts.githubbrowser.data.remote

import com.rudearts.githubbrowser.data.model.RepositoryRest
import com.rudearts.githubbrowser.data.model.SearchResponse
import com.rudearts.githubbrowser.data.model.StarredRest
import com.rudearts.githubbrowser.data.model.UserRest
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RestApi {

    @GET("search/repositories")
    fun getRepositories(@Query("q") name:String): Single<SearchResponse<RepositoryRest>>

    @GET("search/users")
    fun getUsers(@Query("q") name:String): Single<SearchResponse<UserRest>>

    @GET("users/{userName}/starred")
    fun getUserStars(@Path("userName") name:String): Single<List<StarredRest?>>

    @GET("users/{userName}")
    fun getUser(@Path("userName") name:String): Single<UserRest>

}