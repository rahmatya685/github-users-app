package com.znggis.githubusersapp.repo.local

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.znggis.githubusersapp.repo.local.entity.RemoteKeys
import com.znggis.githubusersapp.util.DbFactory
import com.znggis.githubusersapp.util.ItemFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config



val remoteKey =  RemoteKeys(
    itemId = 100,
    nextKey = 3,
    prevKey = 1
)

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class LocalDatasourceTest {

    private lateinit var database: Database
    private lateinit var dataSource: LocalDatasource

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        database =  DbFactory.buildDb()
        dataSource = DefaultLocalDatasource(
            database
        )
    }

    @After
    fun cleanUp() {
        database.close()
        Dispatchers.resetMain()
    }


    @Test
    fun insertItems_getList_checkEquality() = runBlocking {
        val itemEntity = ItemFactory.generateItem()
        //WHEN - inserting items
        dataSource.insertItems(listOf(itemEntity))

        //WHEN - retrieving items
        val result = dataSource.getAllItemsPagingSource()

        //THEN - check items are correct
        assertThat(
            result.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 1,
                    placeholdersEnabled = false,
                )
            )
        ).isEqualTo(
            PagingSource.LoadResult.Page(
                data = listOf(itemEntity),
                prevKey = null,
                nextKey = null,
                itemsAfter = 0,
                itemsBefore = 0
            )
        )

    }

    @Test
    fun deleteItems_getList_checkIsEmpty() = runBlocking {
        val itemEntity = ItemFactory.generateItem()
        //WHEN - deleting items
        dataSource.insertItems(listOf(itemEntity))
        dataSource.deleteAllItems()

        //WHEN - retrieving items
        val result = dataSource.getAllItemsPagingSource()

        //THEN - check items are correct
        assertThat(
            result.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 1,
                    placeholdersEnabled = false,
                )
            )
        ).isEqualTo(
            PagingSource.LoadResult.Page(
                data = listOf(),
                prevKey = null,
                nextKey = null,
                itemsAfter = 0,
                itemsBefore = 0
            )
        )

    }

    @Test
    fun insertRemoteKey_findRemote_checkIsValid() = runBlocking {
        //WHEN - inserting remote key
        database.clearAllTables()
        val remoteKey =  RemoteKeys(
            itemId = 100,
            nextKey = 3,
            prevKey = 1
        )
        dataSource.insertAllKeys(listOf(remoteKey))

        //WHEN - retrieving remote key
        val result = dataSource.remoteKeysItemId(remoteKey.itemId)

        //THEN - check remote key are correct
        assertThat(result?.itemId).isEqualTo(remoteKey.itemId)
        assertThat(result?.nextKey).isEqualTo(remoteKey.nextKey)
        assertThat(result?.prevKey).isEqualTo(remoteKey.prevKey)

    }


    @Test
    fun clearRemoteKeys_getListOfKeys_checkIsEmpty()= runBlocking {
        //WHEN - deleting keys
        database.clearAllTables()
        dataSource.insertAllKeys(listOf(remoteKey))
        dataSource.clearRemoteKeys()

        //WHEN - retrieving remote key
        val result = dataSource.remoteKeysItemId(remoteKey.itemId)

        //THEN - check remote key are correct
        assertThat(result).isNull()
    }

}