package com.example.data.source.remote

import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient

object NetworkClient {

    private const val BASE_URL = "https://graphqlzero.almansi.me/api"

    fun createHttpClient() = OkHttpClient.Builder()
        .build()

    fun createNetworkClient() = ApolloClient.builder()
        .okHttpClient(createHttpClient())
        .serverUrl(BASE_URL)
        .build()
}