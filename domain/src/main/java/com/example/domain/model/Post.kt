package com.example.domain.model

data class Post(
    val title: String = "",
    val body: String = "",
    val user: User = User("", "")
)
