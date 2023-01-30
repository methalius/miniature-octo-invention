package com.doivid.githubclient.data

import com.doivid.githubclient.api.GithubService
import com.doivid.githubclient.api.models.ApiUserListItem
import com.doivid.githubclient.domain.UserDetails
import javax.inject.Inject

class GithubRepository @Inject constructor(
    private val service: GithubService
) {
    suspend fun getUsers(since: Long): List<ApiUserListItem> {
        return service.listUsers(since, 30)
    }

    suspend fun getUser(userLogin: String): UserDetails {
        return service.getUser(userLogin).let {
            UserDetails(
                it.login,
                it.name,
                it.bio,
                it.id,
                it.avatarUrl,
                it.company,
                it.location,
                it.blog,
                it.twitterUsername,
                it.email,
                it.followers,
                it.following,
                it.publicRepos
            )
        }
    }
}