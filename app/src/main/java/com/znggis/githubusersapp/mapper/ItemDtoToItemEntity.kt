package com.znggis.githubusersapp.mapper

import com.znggis.githubusersapp.repo.local.entity.ItemEntity
import com.znggis.githubusersapp.repo.remote.dto.user.ItemDto

class ItemDtoToItemEntity : Mapper<ItemDto, ItemEntity>() {
    override fun convert(it: ItemDto): ItemEntity = ItemEntity(
        itemId = it.id,
        score = it.score,
        isFavourite = false,
        avatarUrl = it.avatar_url,
        item = it.login
    )
}