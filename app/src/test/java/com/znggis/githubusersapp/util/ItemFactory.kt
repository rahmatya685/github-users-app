package com.znggis.githubusersapp.util

import com.znggis.githubusersapp.repo.local.entity.ItemEntity

object ItemFactory {

    fun generateItem(): ItemEntity = ItemEntity(
        itemId = 100,
        item = "Hid",
        avatarUrl = "amazing url",
        isFavourite = false,
        score = 100
    ).apply {
        id = 100
    }
}