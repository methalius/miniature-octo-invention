package com.doivid.githubclient.api

import com.doivid.githubclient.api.models.ApiUserDetails
import com.doivid.githubclient.api.models.ApiUserListItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    @GET("/users")
    suspend fun listUsers(
        @Query("since") since: Long?,
        @Query("per_page") perPage: Int?
    ): List<ApiUserListItem>

    @GET("/users/{userLogin}")
    suspend fun getUser(@Path("userLogin") userLogin: String): ApiUserDetails
}