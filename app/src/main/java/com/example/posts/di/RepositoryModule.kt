package com.example.posts.di

import com.example.data.repository.PostsRepositoryImpl
import com.example.domain.repository.PostsRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<PostsRepository> { PostsRepositoryImpl(get()) }
}