package com.example.domain.model

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure(val errors: List<Error>) : Result<Nothing>()
}