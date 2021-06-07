package com.znggis.githubusersapp.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.znggis.githubusersapp.databinding.ItemGithubuserBinding
import com.znggis.githubusersapp.repo.model.GitHubItem
import com.znggis.githubusersapp.ui.image.ImageLoader

class ItemViewHolder(
    private val binding: ItemGithubuserBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(model: GitHubItem?, clickListener: (GitHubItem) -> Unit, imageLoader: ImageLoader) {
        binding.root.setOnClickListener {
            model?.let { clickListener.invoke(model) }
        }
        binding.item = model
        model?.let {
            imageLoader.loadImage(binding.imgUser,model.avatarUrl)
        }

    }

    fun updateScore(item: GitHubItem?) {
        binding.item =item
    }
}