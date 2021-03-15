package com.example.posts.presentation
import com.example.domain.model.Error

sealed class UiState<out T> {

    object Loading : UiState<Nothing>()
    object Initial : UiState<Nothing>()

    abstract class Success<T> : UiState<T>() {
        data class HasData<T>(val data: T) : Success<T>()
    }

    abstract class Failure : UiState<Nothing>() {
        data class AllowRetry(val error: Error) : Failure()
    }
}