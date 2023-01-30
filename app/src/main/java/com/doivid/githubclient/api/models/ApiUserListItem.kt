package com.doivid.githubclient.api.models

import com.google.gson.annotations.SerializedName

data class ApiUserListItem(
    val login: String,
    val id: Long,
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    val url: String
)