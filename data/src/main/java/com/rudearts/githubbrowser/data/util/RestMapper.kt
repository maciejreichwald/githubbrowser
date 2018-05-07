package com.rudearts.githubbrowser.data.util

import com.rudearts.githubbrowser.data.model.RepositoryRest
import com.rudearts.githubbrowser.data.model.SearchResponse
import com.rudearts.githubbrowser.data.model.StarredRest
import com.rudearts.githubbrowser.data.model.UserRest
import com.rudearts.githubbrowser.domain.model.RepoItem
import com.rudearts.githubbrowser.domain.model.RepoItemType
import com.rudearts.githubbrowser.domain.model.User
import javax.inject.Inject

class RestMapper @Inject constructor() {

    fun searchUser2repoItems(response: SearchResponse<UserRest>) =
            response.items?.map { user -> user2repoItem(user) } ?: emptyList()

    fun searchRespository2repoItems(response: SearchResponse<RepositoryRest>) =
            response.items?.map { repo -> repo2repoItem(repo) } ?: emptyList()

    fun rest2user(user:UserRest) = with(user) {
        User(id, nonNull(login), avatar, followers)
    }

    fun stars2starsCount(stars:List<StarredRest?>):Int {
        var count = 0
        stars.forEach { item ->
            item.let {
                count += item?.stars ?: 0
            }
        }
        return count
    }

    private fun repo2repoItem(repo: RepositoryRest) = with(repo) {
        RepoItem(id, nonNull(name), owner?.avatar, RepoItemType.REPOSITORY)
    }

    private fun user2repoItem(user: UserRest) = with(user) {
        RepoItem(id, nonNull(login), avatar, RepoItemType.USER)
    }

    private fun nonNull(string: String?) = string ?: ""

}