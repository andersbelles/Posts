package com.example.posts.presentation.posts

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.posts.R
import com.example.posts.databinding.FragmentPostsBinding
import com.example.posts.databinding.LayoutErrorBinding
import com.example.posts.presentation.posts.adapter.PostLoadStateAdapter
import com.example.posts.presentation.posts.adapter.PostsAdapter
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class PostsFragment : Fragment(R.layout.fragment_posts) {

    private val binding by viewBinding(FragmentPostsBinding::bind)
    private val errorLayoutBinding by viewBinding(LayoutErrorBinding::bind)

    private val viewModel: PostsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postsAdapter = PostsAdapter { onPostPreviewClicked(it.id) }

        postsAdapter.addLoadStateListener {
            initWithLoadState(it.refresh)
        }

        errorLayoutBinding.retryButton.setOnClickListener {
            postsAdapter.refresh()
        }

        binding.postsRecyclerView.apply {
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(context)
            adapter = postsAdapter.withLoadStateHeaderAndFooter(
                PostLoadStateAdapter(postsAdapter),
                PostLoadStateAdapter(postsAdapter)
            )
        }

        lifecycleScope.launchWhenStarted {
            viewModel.posts.collect {
                postsAdapter.submitData(it)
            }
        }

    }

    private fun initWithLoadState(loadState: LoadState) {
        binding.postsRecyclerView.isVisible = loadState is LoadState.NotLoading
        binding.progressIndicator.isVisible = loadState is LoadState.Loading
        errorLayoutBinding.retryButton.isVisible = loadState is LoadState.Error
        errorLayoutBinding.errorTextView.isVisible = loadState is LoadState.Error

        if (loadState is LoadState.Error) {
            errorLayoutBinding.errorTextView.text = loadState.error.message
        }
    }

    private fun onPostPreviewClicked(postId: String) {
        val startPostDetailsFragment = PostsFragmentDirections.actionNavigateToPostDetails(postId)
        findNavController().navigate(startPostDetailsFragment)
    }
}