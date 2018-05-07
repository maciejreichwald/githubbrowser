package com.rudearts.githubbrowser.domain.usecase

import com.rudearts.githubbrowser.domain.model.User
import com.rudearts.githubbrowser.domain.remote.RemoteCallable
import com.rudearts.githubbrowser.domain.remote.UserCache
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class LoadUserUseCase @Inject constructor(
        private val remoteCallable: RemoteCallable,
        private val cache: UserCache) {

    fun loadUser(name:String):Single<User> = when(cache.hasUser(name)) {
        true -> Single.fromCallable { cache.loadUser(name) }
        else -> Single.zip(
                remoteCallable.getUser(name),
                remoteCallable.getStarsCount(name),
                BiFunction { user, starsCount -> handleLoadedUser(user, starsCount)})
    }

    internal fun handleLoadedUser(user: User, starsCount: Int) =
            user.copy(stars = starsCount).apply {
                cache.saveUser(this)
            }

}