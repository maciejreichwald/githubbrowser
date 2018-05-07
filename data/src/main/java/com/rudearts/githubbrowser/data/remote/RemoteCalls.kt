package com.rudearts.githubbrowser.data.remote

import com.rudearts.githubbrowser.data.util.RestMapper
import com.rudearts.githubbrowser.domain.model.RepoItem
import com.rudearts.githubbrowser.domain.model.User
import com.rudearts.githubbrowser.domain.remote.RemoteCallable
import io.reactivex.Single
import javax.inject.Inject

class RemoteCalls @Inject constructor(
        private val restApi: RestApi,
        private val mapper: RestMapper) : RemoteCallable {

    override fun search4User(name: String):Single<List<RepoItem>> = restApi.getUsers(name)
                    .map { response -> mapper.searchUser2repoItems(response) }


    override fun search4Repository(name: String):Single<List<RepoItem>> = restApi.getRepositories(name)
            .map { response -> mapper.searchRespository2repoItems(response) }

    override fun getStarsCount(userName: String): Single<Int> = restApi.getUserStars(userName)
            .map { stars -> mapper.stars2starsCount(stars) }

    override fun getUser(userName: String): Single<User> = restApi.getUser(userName)
            .map { user -> mapper.rest2user(user) }
}