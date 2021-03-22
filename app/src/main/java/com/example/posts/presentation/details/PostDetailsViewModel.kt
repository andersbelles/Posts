package com.example.posts.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Post
import com.example.domain.model.Result
import com.example.domain.repository.PostsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostDetailsViewModel(private val postsRepository: PostsRepository) : ViewModel() {

    private var _viewState = MutableStateFlow<ViewState<Post>>(ViewState.Initial)
    val viewState: StateFlow<ViewState<Post>> get() = _viewState

    fun fetchPost(id: String) = viewModelScope.launch {
        _viewState.value = ViewState.Loading
        when (val result = postsRepository.getPost(id)){
            is Result.Success -> _viewState.value = ViewState.Success.HasData(result.data)
            is Result.Failure -> _viewState.value = ViewState.Failure.AllowRetry(result.errors[0])
        }
    }

}