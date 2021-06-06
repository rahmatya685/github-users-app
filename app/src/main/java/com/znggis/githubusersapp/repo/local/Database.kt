package com.znggis.githubusersapp.repo.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.znggis.githubusersapp.repo.local.dao.ItemDao
import com.znggis.githubusersapp.repo.local.dao.RemoteKeysDao
import com.znggis.githubusersapp.repo.local.entity.ItemEntity
import com.znggis.githubusersapp.repo.local.entity.RemoteKeys

const val DB_VERSION = 1
const val DB_NAME = "Db"

@Database(entities = [ItemEntity::class,RemoteKeys::class], version = DB_VERSION)
abstract class Database : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun remoteKeysDao():RemoteKeysDao
}