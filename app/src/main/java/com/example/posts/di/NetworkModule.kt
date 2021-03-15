package com.example.posts.di

import com.example.data.source.remote.NetworkClient
import org.koin.dsl.module

val networkModule = module {
    single { NetworkClient.createNetworkClient() }
}
