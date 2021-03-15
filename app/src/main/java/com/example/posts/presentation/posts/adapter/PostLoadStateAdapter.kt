package com.example.posts.presentation.posts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.posts.R
import com.example.posts.databinding.ItemLoadingStateBinding
import com.example.posts.databinding.LayoutErrorBinding

class PostLoadStateAdapter(
    private val adapter: PostsAdapter
) : LoadStateAdapter<PostLoadStateAdapter.NetworkStateItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        NetworkStateItemViewHolder(
            ItemLoadingStateBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_loading_state,
                    parent,
                    false
                )
            )
        ) { adapter.retry() }

    override fun onBindViewHolder(holder: NetworkStateItemViewHolder, loadState: LoadState) =
        holder.bind(loadState)

    class NetworkStateItemViewHolder(
        private val binding: ItemLoadingStateBinding,
        private val errorLayoutBinding: LayoutErrorBinding = LayoutErrorBinding.bind(binding.root),
        private val retryCallback: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            errorLayoutBinding.retryButton.setOnClickListener {
                it.isVisible = false
                errorLayoutBinding.errorTextView.isVisible = false
                retryCallback()
            }
        }

        fun bind(loadState: LoadState) {
            with(binding) {
                progressIndicator.isVisible = loadState is LoadState.Loading
                errorLayoutBinding.retryButton.isVisible = loadState is LoadState.Error
                errorLayoutBinding.errorTextView.isVisible = loadState is LoadState.Error
                errorLayoutBinding.errorTextView.text = (loadState as? LoadState.Error)?.error?.message
            }
        }
    }
}