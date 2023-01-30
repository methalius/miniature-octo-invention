package com.doivid.githubclient.domain

import android.net.Uri

data class UserListingEntry(
    val login: String,
    val avatarUrl: String?,
    val profileUrl: String
)

data class UserDetails(
    val login: String,
    val name: String,
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
