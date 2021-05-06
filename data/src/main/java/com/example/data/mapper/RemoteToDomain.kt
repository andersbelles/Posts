package com.example.data.mapper

import com.example.data.GetPostPreviewsQuery
import com.example.data.GetPostQuery
import com.example.domain.model.Error
import com.example.domain.model.Post
import com.example.domain.model.PostPreview
import com.example.domain.model.User
import java.lang.Exception

fun GetPostPreviewsQuery.Posts.toDomain(): List<PostPreview> {
    return this.data?.map {
        PostPreview(
            it?.id.orEmpty(),
            it?.title.orEmpty(),
            it?.body.orEmpty()
        )
    } ?: emptyList()
}

fun GetPostQuery.Post.toDomain(): Post {
    return Post(
        title.orEmpty(),
        body.orEmpty(),
        User(
            user?.username.orEmpty(),
            user?.name.orEmpty()
        )
    )
}

fun com.apollographql.apollo.api.Error.toDomain(): Error {
    return Error.Server(message, Exception(this.message))
}