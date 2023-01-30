package com.doivid.githubclient.data

import com.doivid.githubclient.api.GithubService
import com.doivid.githubclient.api.models.ApiUserListItem
import com.doivid.githubclient.domain.GithubEvent
import com.doivid.githubclient.domain.Repository
import com.doivid.githubclient.domain.UserDetails
import com.doivid.githubclient.domain.UserListingEntry
import javax.inject.Inject

class GithubRepository @Inject constructor(
    private val service: GithubService
) {
    private fun ApiUserListItem.toModel(): UserListingEntry =
        UserListingEntry(
            id,
            login,
            avatarUrl,
            url
        )

    suspend fun getUsers(since: Long): List<UserListingEntry> {
        return service.listUsers(since, 30).map { it.toModel() }
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

    suspend fun getEventsForUser(userLogin: String): List<GithubEvent> {
        return service.getUserPublicEvents(userLogin).map {
            GithubEvent(
                type = it.type,
                actor = it.actor.toModel(),
                repository = it.repository?.let { apiRepo ->
                    Repository(apiRepo.id, apiRepo.name, apiRepo.url)
                },
                createdAt = it.createdAt
            )
        }
    }
}