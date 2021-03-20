package com.example.posts.presentation.details
import com.example.domain.model.Error

sealed class ViewState<out T> {

    object Loading : ViewState<Nothing>()
    object Initial : ViewState<Nothing>()

    abstract class Success<T> : ViewState<T>() {
        data class HasData<T>(val data: T) : Success<T>()
    }

    abstract class Failure : ViewState<Nothing>() {
        data class AllowRetry(val error: Error) : Failure()
    }
}