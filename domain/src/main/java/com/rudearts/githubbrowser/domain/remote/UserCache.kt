package com.rudearts.githubbrowser.domain.remote

import com.rudearts.githubbrowser.domain.model.User

interface UserCache {
    fun loadUser(name: String): User
    fun saveUser(user: User)
    fun hasUser(name: String): Boolean
}