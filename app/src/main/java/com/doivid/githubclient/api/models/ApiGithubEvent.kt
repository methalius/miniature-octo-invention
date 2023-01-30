package com.doivid.githubclient.api.models

import com.doivid.githubclient.domain.GithubEventType
import com.google.gson.annotations.SerializedName
import java.time.OffsetDateTime

data class ApiGithubEvent(
    val type: GithubEventType,
    val actor: ApiUserListItem,
    @SerializedName("repo")
    val repository: ApiRepository?,
    @SerializedName("created_at")
    val createdAt: OffsetDateTime
)