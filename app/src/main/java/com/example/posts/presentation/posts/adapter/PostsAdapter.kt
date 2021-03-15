package com.example.posts.presentation.posts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.ContentLoadingProgressBar
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.PostPreview
import com.example.posts.databinding.ItemPostBinding


class PostsAdapter(private val onItemClick: (PostPreview) -> Unit) : PagingDataAdapter<PostPreview, PostsAdapter.ViewHolder>(
    RepoDiffUtilItemCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding) { position ->
            getItem(position)?.also {
                onItemClick(it)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { post ->
            holder.binding.postTitleTextView.text = post.title
            holder.binding.postBodyTextView.text = post.body
        }
    }

    class ViewHolder(val binding: ItemPostBinding, onItemClicked: (Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener { onItemClicked(bindingAdapterPosition) }
        }
    }

}
