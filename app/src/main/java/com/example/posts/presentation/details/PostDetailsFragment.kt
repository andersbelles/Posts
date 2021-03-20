package com.example.posts.presentation.details

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
            if (viewState.value is ViewState.Initial) {
                fetchPost(args.postId)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.viewState.collect {
                initWithUiState(it)
            }
        }

        errorLayoutBinding.retryButton.setOnClickListener {
            viewModel.fetchPost(args.postId)
        }
    }

    private fun initWithUiState(viewState: ViewState<Post>) {
        binding.usernameTextView.isVisible = viewState is ViewState.Success.HasData
        binding.titleTextView.isVisible = viewState is ViewState.Success.HasData
        binding.bodyTextView.isVisible = viewState is ViewState.Success.HasData
        binding.fullnameTextView.isVisible = viewState is ViewState.Success.HasData

        binding.progressIndicator.isVisible = viewState is ViewState.Loading

        errorLayoutBinding.errorTextView.isVisible = viewState is ViewState.Failure.AllowRetry
        errorLayoutBinding.retryButton.isVisible = viewState is ViewState.Failure.AllowRetry

        when (viewState) {
            is ViewState.Success.HasData -> {
                binding.usernameTextView.text =
                    getString(R.string.post_details_username_format, viewState.data.user.username)
                binding.titleTextView.text = viewState.data.title
                binding.bodyTextView.text = viewState.data.body
                binding.fullnameTextView.text = viewState.data.user.fullName
            }
            is ViewState.Failure.AllowRetry -> errorLayoutBinding.errorTextView.text = viewState.error.message
        }
    }
}