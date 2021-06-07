package com.znggis.githubusersapp.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.znggis.githubusersapp.databinding.ItemGithubuserBinding
import com.znggis.githubusersapp.repo.model.GitHubItem
import com.znggis.githubusersapp.ui.image.ImageLoader

class ItemViewHolder(
    private val binding: ItemGithubuserBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        model: GitHubItem?,
        clickListener: (EventType) -> Unit,
        imageLoader: ImageLoader
    ) = with(binding) {
        item = model
        root.setOnClickListener {
            model?.let { clickListener.invoke(EventType.ShowDetail(model)) }
        }
        btnLike.setOnClickListener {
            model?.let { clickListener.invoke(EventType.OnFavouriteClicked(model)) }
        }
        model?.let {
            imageLoader.loadImage(binding.imgUser, model.avatarUrl)
        }

    }

    fun updateScore(item: GitHubItem?) {
        binding.item = item
    }
}