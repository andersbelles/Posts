package com.example.data.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import com.example.data.GetPostPreviewsQuery
import com.example.data.GetPostQuery
import com.example.data.ext.safeApiCall
import com.example.data.mapper.toDomain
import com.example.data.type.PageQueryOptions
import com.example.data.type.PaginateOptions
import com.example.domain.model.Post
import com.example.domain.model.PostPreview
import com.example.domain.model.Result
import com.example.domain.repository.PostsRepository

class PostsRepositoryImpl(private val networkClient: ApolloClient) : PostsRepository {

    override suspend fun getPosts(pageNumber: Int, pageSize: Int): Result<List<PostPreview>> {
        val pageQueryOptions = Input.fromNullable(
            PageQueryOptions(
                Input.fromNullable(
                    PaginateOptions(
                        Input.fromNullable(pageNumber), Input.fromNullable(pageSize)
                    )
                )
            )
        )
        val result =
            safeApiCall { networkClient.query(GetPostPreviewsQuery(pageQueryOptions)).await() }
        return when (result) {
            is Result.Success -> Result.Success(result.data.posts?.toDomain()!!)
            is Result.Failure -> result
        }
    }

    override suspend fun getPost(id: String): Result<Post> {
        val result = safeApiCall { networkClient.query(GetPostQuery(id)).await() }
        return when (result) {
            is Result.Success -> Result.Success(result.data.post?.toDomain() ?: Post())
            is Result.Failure -> result
        }
    }
}