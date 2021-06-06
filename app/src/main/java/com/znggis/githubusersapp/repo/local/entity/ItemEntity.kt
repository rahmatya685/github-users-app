package com.znggis.githubusersapp.repo.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = ItemEntity.TB_NAME)
data class ItemEntity(

    @ColumnInfo(name = F_ITEM_ID, typeAffinity = ColumnInfo.INTEGER)
    var itemId: Int,

    @ColumnInfo(name = F_LOGIN, typeAffinity = ColumnInfo.TEXT)
    val item: String,

    @ColumnInfo(name = F_AVATAR_URL, typeAffinity = ColumnInfo.TEXT)
    val avatarUrl: String,


    @ColumnInfo(name = F_SCORE, typeAffinity = ColumnInfo.INTEGER)
    val score: Int,


    @ColumnInfo(name = F_IS_FAVOURITE, typeAffinity = ColumnInfo.INTEGER)
    val isFavourite: Boolean,

    ) {

    @ColumnInfo(name = F_ID)
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    companion object {
        const val TB_NAME = "ITEM"
        const val F_LOGIN = "LOGIN"
        const val F_AVATAR_URL = "AVATAR_URL"
        const val F_SCORE = "SCORE"
        const val F_IS_FAVOURITE = "IS_FAVOURITE"
        const val F_ID = "ID"
        const val F_ITEM_ID = "ITEM_ID"
    }
}