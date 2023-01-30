package com.doivid.githubclient.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = getToken()
        val request = if (token != null) {
            chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }
        return chain.proceed(request)
    }

    /**
     * Actually implement token fetching from some storage device
     * TODO: Insert your github token to increase the amount of requests that can be done before
     * you start receiving 403s
     */
    @Suppress("RedundantNullableReturnType")
    fun getToken(): String? = null
}