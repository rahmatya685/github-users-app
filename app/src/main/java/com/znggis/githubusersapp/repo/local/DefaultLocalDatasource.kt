package com.znggis.githubusersapp.repo.local

import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.znggis.githubusersapp.repo.local.entity.ItemEntity
import com.znggis.githubusersapp.repo.local.entity.RemoteKeys
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultLocalDatasource
@Inject constructor(
    private val database: Database
) : LocalDatasource {

    private val itemDao = database.itemDao()
    private val remoteKeysDao = database.remoteKeysDao()

    override suspend fun insertItems(items: List<ItemEntity>) =
        itemDao.insert(*items.toTypedArray())

    override suspend fun deleteAllItems() =
        itemDao.deleteAll()

    override fun getAllItemsPagingSource(): PagingSource<Int, ItemEntity> =
        itemDao.getItems()

    override suspend fun applyOnDbTransaction(doOnTransaction: suspend () -> Unit) =
        database.withTransaction { doOnTransaction.invoke() }

    override suspend fun remoteKeysItemId(id: Long): RemoteKeys? =
        remoteKeysDao.remoteKeysItemId(id)

    override suspend fun clearRemoteKeys() =
        remoteKeysDao.clearRemoteKeys()

    override suspend fun insertAllKeys(keys: List<RemoteKeys>) =
        remoteKeysDao.insertAll(keys)

    override suspend fun toggleItemFav(itemLocalId: Int) {
        itemDao.findById(itemLocalId)?.let {
            itemDao.toggleItemFav(itemLocalId, if (it.isFavourite) 0 else 1)
        }
    }

    override fun findItem(itemId: Int):
            Flow<ItemEntity?> = itemDao.findByIdFlow(itemId)

}