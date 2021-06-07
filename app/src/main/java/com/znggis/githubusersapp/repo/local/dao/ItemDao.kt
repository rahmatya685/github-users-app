package com.znggis.githubusersapp.repo.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.znggis.githubusersapp.repo.local.entity.ItemEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ItemDao : BaseDao<ItemEntity> {

    @Query("SELECT * FROM ITEM")
    fun getItems(): PagingSource<Int, ItemEntity>

    @Query("DELETE FROM ITEM")
    suspend fun deleteAll()

    @Query("SELECT * FROM ITEM WHERE ITEM.ID= :id")
    fun findById(id: Int): ItemEntity?


    @Query("SELECT * FROM ITEM WHERE ITEM.ID= :id")
    fun findByIdFlow(id: Int): Flow<ItemEntity?>

    @Query("UPDATE ITEM SET IS_FAVOURITE = :isFav WHERE ID=:itemLocalId")
    fun toggleItemFav(itemLocalId: Int, isFav: Int)

}