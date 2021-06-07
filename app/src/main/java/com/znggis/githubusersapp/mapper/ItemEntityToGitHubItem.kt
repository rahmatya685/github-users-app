package com.znggis.githubusersapp.mapper

import com.znggis.githubusersapp.repo.local.entity.ItemEntity
import com.znggis.githubusersapp.repo.model.GitHubItem
import javax.inject.Inject

class ItemEntityToGitHubItem
@Inject constructor() : Mapper<ItemEntity, GitHubItem>() {
    override fun convert(it: ItemEntity): GitHubItem =
        GitHubItem(
            id = it.id,
            item = it.item,
            avatarUrl = it.avatarUrl,
            score = it.score,
            isFavourite = it.isFavourite
        )
}