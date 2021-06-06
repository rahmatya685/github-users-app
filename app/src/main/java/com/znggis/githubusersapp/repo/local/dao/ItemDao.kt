package com.znggis.githubusersapp.repo.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.znggis.githubusersapp.repo.local.entity.ItemEntity


@Dao
interface ItemDao : BaseDao<ItemEntity> {

    @Query("SELECT * FROM ITEM")
    fun getItems(): PagingSource<Int,ItemEntity>

    @Query("DELETE FROM ITEM")
    suspend fun deleteAll()

    @Query("SELECT * FROM ITEM WHERE ITEM.ID= :id")
    fun findById(id: Int): ItemEntity?

}