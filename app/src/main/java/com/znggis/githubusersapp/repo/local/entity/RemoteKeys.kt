package com.znggis.githubusersapp.repo.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val itemId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)