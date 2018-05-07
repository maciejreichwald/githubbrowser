package com.rudearts.githubbrowser.domain.usecase

import com.rudearts.githubbrowser.domain.model.RepoItem
import com.rudearts.githubbrowser.domain.remote.RemoteCallable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class Search4RepoItemsUseCase @Inject constructor(
        private val remoteCallable: RemoteCallable) {

    fun search(name:String):Single<List<RepoItem>> = Single.zip(
            remoteCallable.search4Repository(name),
            remoteCallable.search4User(name),
            BiFunction { repos, users ->
                val items = repos + users
                items.sortedBy { it.id }
            })

}
