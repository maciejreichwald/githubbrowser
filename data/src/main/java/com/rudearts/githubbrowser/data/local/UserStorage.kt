package com.rudearts.githubbrowser.data.local

import com.rudearts.githubbrowser.domain.model.User
import com.rudearts.githubbrowser.domain.remote.UserCache

class UserStorage : UserCache {

    private val map = mutableMapOf<String, User>()

    override fun loadUser(name: String) = map.getValue(name)

    override fun saveUser(user: User) {
        map.put(user.name, user)
    }

    override fun hasUser(name: String) = map.containsKey(name)
}