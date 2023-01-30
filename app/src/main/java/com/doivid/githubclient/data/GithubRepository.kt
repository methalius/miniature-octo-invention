package com.doivid.githubclient.data

import com.doivid.githubclient.api.GithubService
import com.doivid.githubclient.api.models.ApiUserListItem
import javax.inject.Inject

class GithubRepository @Inject constructor(
    private val service: GithubService
) {
    suspend fun getUsers(since: Long): List<ApiUserListItem> {
        return service.listUsers(since, 30)
    }
}