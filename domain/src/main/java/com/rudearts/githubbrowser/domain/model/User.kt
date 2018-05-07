package com.rudearts.githubbrowser.domain.model

data class User(val id: Long, val name: String, val avatar: String?, val stars: Int=0, val followers: Int=0)