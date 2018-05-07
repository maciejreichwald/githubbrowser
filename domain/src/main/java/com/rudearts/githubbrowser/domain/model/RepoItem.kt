package com.rudearts.githubbrowser.domain.model

data class RepoItem(val id: Long,
                    val name: String,
                    val avatar: String?,
                    val type: RepoItemType)