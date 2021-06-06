package com.znggis.githubusersapp.repo.local

import androidx.paging.PagingSource
import com.znggis.githubusersapp.repo.local.entity.ItemEntity
import com.znggis.githubusersapp.repo.local.entity.RemoteKeys


interface LocalDatasource {

    suspend fun insertItems(items: List<ItemEntity>)

    suspend fun deleteAllItems()

    fun getAllItemsPagingSource(): PagingSource<Int, ItemEntity>

    suspend fun applyOnDbTransaction(doOnTransaction: suspend () -> Unit)

    suspend fun remoteKeysItemId(id: Long): RemoteKeys?

    suspend fun clearRemoteKeys()

    suspend  fun insertAllKeys(keys: List<RemoteKeys>)
}