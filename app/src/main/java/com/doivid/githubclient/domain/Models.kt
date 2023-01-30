package com.doivid.githubclient.domain

import java.time.OffsetDateTime

data class UserListingEntry(
    val id: Long,
    val login: String,
    val avatarUrl: String?,
    val profileUrl: String
)

data class UserDetails(
    val login: String,
    val name: String?,
    val bio: String?,
    val id: Long,
    val avatarUrl: String?,
    val company: String?,
    val location: String?,
    val blog: String?,
    val twitterHandle: String?,
    val email: String?,
    val followers: Int,
    val following: Int,
    val publicRepositories: Int
)

data class Repository(
    val id: Long,
    val name: String,
    val url: String
)

enum class GithubEventType {
    CommitCommentEvent,
    CreateEvent,
    DeleteEvent,
    ForkEvent,
    GollumEvent,
    IssueCommentEvent,
    IssuesEvent,
    MemberEvent,
    PublicEvent,
    PullRequestEvent,
    PullRequestReviewEvent,
    PullRequestReviewCommentEvent,
    PullRequestReviewThreadEvent,
    PushEvent,
    ReleaseEvent,
    SponsorshipEvent,
    WatchEvent
}

data class GithubEvent(
    val type: GithubEventType,
    val actor: UserListingEntry,
    val repository: Repository?,
    val createdAt: OffsetDateTime
)