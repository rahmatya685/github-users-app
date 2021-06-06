package com.znggis.githubusersapp.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.znggis.githubusersapp.databinding.ItemGithubuserBinding
import com.znggis.githubusersapp.repo.model.GitHubItem

class ItemViewHolder(
    private val binding: ItemGithubuserBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(model: GitHubItem?) {
        binding.item = model
    }

    fun updateScore(item: GitHubItem?) {
        binding.item =item
    }
}