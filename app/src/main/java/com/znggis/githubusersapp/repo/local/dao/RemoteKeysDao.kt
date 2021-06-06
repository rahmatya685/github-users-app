package com.znggis.githubusersapp.repo.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.znggis.githubusersapp.repo.local.entity.RemoteKeys

@Dao
interface RemoteKeysDao:BaseDao<RemoteKeys> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE itemId = :itemId")
    suspend fun remoteKeysItemId(itemId: Long): RemoteKeys?

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()

}