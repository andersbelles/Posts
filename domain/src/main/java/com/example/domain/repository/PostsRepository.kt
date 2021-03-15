package com.example.domain.repository

import com.example.domain.model.Post
import com.example.domain.model.PostPreview
import com.example.domain.model.Result

interface PostsRepository {
    suspend fun getPosts(pageNumber: Int, pageSize: Int): Result<List<PostPreview>>

    suspend fun getPost(id: String): Result<Post>
}