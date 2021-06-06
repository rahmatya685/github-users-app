package com.znggis.githubusersapp.repo.local

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.znggis.githubusersapp.repo.local.entity.ItemEntity
import com.znggis.githubusersapp.util.DbFactory
import com.znggis.githubusersapp.util.ItemFactory
import com.znggis.githubusersapp.util.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config





@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class ItemDaoTest {

    private lateinit var database: Database

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @Before
    fun initDb() {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        database = DbFactory.buildDb()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertItemAndGetById() = runBlockingTest {
        // GIVEN - insert an item
        val itemEntity = ItemFactory.generateItem()
        database.itemDao().insert(itemEntity)

        // WHEN - Get the item by id from the database
        val loaded = database.itemDao().findById(itemEntity.id)

        // THEN - The loaded data contains the expected values
        MatcherAssert.assertThat<ItemEntity>(loaded, CoreMatchers.notNullValue())
        loaded?.let { loaded ->
            MatcherAssert.assertThat(loaded.id, `is`(itemEntity.id))
            MatcherAssert.assertThat(loaded.item, `is`(itemEntity.item))
            MatcherAssert.assertThat(loaded.avatarUrl, `is`(itemEntity.avatarUrl))
            MatcherAssert.assertThat(loaded.score, `is`(itemEntity.score))
            MatcherAssert.assertThat(loaded.isFavourite, `is`(itemEntity.isFavourite))
        }

    }

    @Test
    fun insertWithReplaceStrategy() = runBlocking {
        val itemEntity = ItemFactory.generateItem()
        // GIVEN - insert  task A
        database.clearAllTables()
        database.itemDao().insert(itemEntity)
        // WHEN - inserting task A again
        database.itemDao().insert(itemEntity)

        // THEN - check new record inserted and no additional record inserted
        val result = database.itemDao().getItems()

        assertThat(
            result.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 2,
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
}