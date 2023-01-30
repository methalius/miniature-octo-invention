package com.doivid.githubclient.di

import com.doivid.githubclient.api.GithubService
import com.doivid.githubclient.api.OffsetDateTimeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.OffsetDateTime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun gson(): Gson =
        GsonBuilder()
            .registerTypeAdapter(OffsetDateTime::class.java, OffsetDateTimeConverter())
            .create()

    @Provides
    @Singleton
    fun retrofit(gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    @Singleton
    fun githubService(retrofit: Retrofit): GithubService =
        retrofit.create(GithubService::class.java)
}