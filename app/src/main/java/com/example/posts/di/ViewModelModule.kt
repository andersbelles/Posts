package com.example.posts.di

import com.example.posts.presentation.details.PostDetailsViewModel
import com.example.posts.presentation.posts.PostsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { PostsViewModel(get()) }
    viewModel { PostDetailsViewModel(get()) }
}