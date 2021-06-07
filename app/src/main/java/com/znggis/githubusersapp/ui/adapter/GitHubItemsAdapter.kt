package com.znggis.githubusersapp.ui.adapter


import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.znggis.githubusersapp.R
import com.znggis.githubusersapp.databinding.ItemGithubuserBinding
import com.znggis.githubusersapp.repo.model.GitHubItem
import com.znggis.githubusersapp.ui.image.ImageLoader
import com.znggis.githubusersapp.ui.inflate
import javax.inject.Inject

class GitHubItemsAdapter @Inject constructor(
    var imageLoader: ImageLoader
) : PagingDataAdapter<GitHubItem, ItemViewHolder>(diffUtilCallback) {

    internal var clickListener: (GitHubItem) -> Unit = { _ -> }


    companion object {
        val diffUtilCallback: DiffUtil.ItemCallback<GitHubItem>
            get() = object : DiffUtil.ItemCallback<GitHubItem>() {
                override fun areItemsTheSame(oldItem: GitHubItem, newItem: GitHubItem): Boolean {
                    return oldItem === newItem && oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: GitHubItem,
                    newItem: GitHubItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemGithubuserBinding.bind(parent.inflate(R.layout.item_githubuser)),
        )
    }


    override fun onBindViewHolder(
        holder: ItemViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val item = getItem(position)
            holder.updateScore(item)
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(getItem(position), clickListener,imageLoader)


}