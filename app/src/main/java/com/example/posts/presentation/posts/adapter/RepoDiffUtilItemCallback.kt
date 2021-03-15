package com.example.posts.presentation.posts.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.PostPreview

class RepoDiffUtilItemCallback : DiffUtil.ItemCallback<PostPreview>() {
    override fun areItemsTheSame(oldItem: PostPreview, newItem: PostPreview): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PostPreview, newItem: PostPreview): Boolean {
        return oldItem == newItem
    }
}