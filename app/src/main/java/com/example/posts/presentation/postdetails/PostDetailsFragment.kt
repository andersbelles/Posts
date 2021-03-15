package com.example.posts.presentation.postdetails

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.domain.model.Post
import com.example.posts.R
import com.example.posts.databinding.FragmentPostDetailsBinding
import com.example.posts.databinding.LayoutErrorBinding
import com.example.posts.presentation.UiState
import kotlinx.coroutines.flow.collect
import org.koin.android.viewmodel.ext.android.viewModel


class PostDetailsFragment : Fragment(R.layout.fragment_post_details) {

    private val args: PostDetailsFragmentArgs by navArgs()

    private val binding by viewBinding(FragmentPostDetailsBinding::bind)
    private val errorLayoutBinding by viewBinding(LayoutErrorBinding::bind)

    private val viewModel: PostDetailsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply {
            if (uiState.value is UiState.Initial) {
                fetchPost(args.postId)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                initWithUiState(it)
            }
        }

        errorLayoutBinding.retryButton.setOnClickListener {
            viewModel.fetchPost(args.postId)
        }
    }

    private fun initWithUiState(uiState: UiState<Post>) {
        binding.usernameTextView.isVisible = uiState is UiState.Success.HasData
        binding.titleTextView.isVisible = uiState is UiState.Success.HasData
        binding.bodyTextView.isVisible = uiState is UiState.Success.HasData
        binding.fullnameTextView.isVisible = uiState is UiState.Success.HasData

        binding.progressIndicator.isVisible = uiState is UiState.Loading

        errorLayoutBinding.errorTextView.isVisible = uiState is UiState.Failure.AllowRetry
        errorLayoutBinding.retryButton.isVisible = uiState is UiState.Failure.AllowRetry

        when (uiState) {
            is UiState.Success.HasData -> {
                binding.usernameTextView.text =
                    getString(R.string.post_details_username_format, uiState.data.user.username)
                binding.titleTextView.text = uiState.data.title
                binding.bodyTextView.text = uiState.data.body
                binding.fullnameTextView.text = uiState.data.user.fullName
            }
            is UiState.Failure.AllowRetry -> errorLayoutBinding.errorTextView.text = uiState.error.message
        }
    }
}