package com.rudearts.githubbrowser.data.model

data class RepositoryRest(
        val id:Long,
        val name:String?,
        val owner:UserRest?
)