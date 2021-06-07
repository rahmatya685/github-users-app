package com.znggis.githubusersapp.di.modules

import android.content.Context
import androidx.room.Room
import com.znggis.githubusersapp.repo.local.Database
import com.znggis.githubusersapp.repo.local.DefaultLocalDatasource
import com.znggis.githubusersapp.repo.local.LocalDatasource
import com.znggis.githubusersapp.repo.local.dao.ItemDao
import com.znggis.githubusersapp.repo.local.dao.RemoteKeysDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface LocalDataModule {

    @get:[Binds Singleton]
    val DefaultLocalDatasource.defLocalDatastore: LocalDatasource

    companion object {
        @Singleton
        @Provides
        fun provideDatabase(@ApplicationContext context: Context): Database =
            Room.databaseBuilder(context, Database::class.java, "Test")
                .build()

        @Singleton
        @Provides
        fun provideItemsDao(database: Database):ItemDao =
            database.itemDao()

        @Singleton
        @Provides
        fun provideRemoteKeysDao(database: Database):RemoteKeysDao =
            database.remoteKeysDao()
    }

}