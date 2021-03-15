package com.example.posts.presentation.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.model.PostPreview
import com.example.domain.repository.PostsRepository
import com.example.posts.presentation.posts.paging.PostsPagingSource
import kotlinx.coroutines.flow.Flow

class PostsViewModel(private val postsRepository: PostsRepository) : ViewModel() {

    companion object {
        const val PAGE_SIZE = 10
        const val PREFETCH_DISTANCE = 3
    }

    val posts: Flow<PagingData<PostPreview>> = Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true,
                prefetchDistance = PREFETCH_DISTANCE
            ),
            pagingSourceFactory = { PostsPagingSource(postsRepository) }
        ).flow.cachedIn(viewModelScope)

}