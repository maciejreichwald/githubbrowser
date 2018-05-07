package com.rudearts.githubbrowser.domain.remote

import com.rudearts.githubbrowser.domain.model.RepoItem
import com.rudearts.githubbrowser.domain.model.User
import io.reactivex.Single

interface RemoteCallable {

    fun search4User(name:String):Single<List<RepoItem>>
    fun search4Repository(name:String):Single<List<RepoItem>>
    fun getStarsCount(userName:String):Single<Int>
    fun getUser(userName:String):Single<User>

}