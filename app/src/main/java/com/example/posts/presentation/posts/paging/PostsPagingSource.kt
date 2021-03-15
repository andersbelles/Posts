package com.example.posts.presentation.posts.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.domain.model.PostPreview
import com.example.domain.model.Result
import com.example.domain.repository.PostsRepository

class PostsPagingSource(private val postsRepository: PostsRepository) : PagingSource<Int, PostPreview>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostPreview> {

        return try {
            val pageNumber = params.key ?: 0
            val pageSize = params.loadSize

            when (val result = postsRepository.getPosts(pageNumber, pageSize)) {
                is Result.Success -> LoadResult.Page(
                    result.data,
                    if (pageNumber > 0) pageNumber - 1 else null,
                    if (result.data.isNotEmpty()) pageNumber + 1 else null
                )
                is Result.Failure -> LoadResult.Error(result.errors[0])
            }

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PostPreview>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

}