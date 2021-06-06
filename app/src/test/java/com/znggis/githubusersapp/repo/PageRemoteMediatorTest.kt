package com.znggis.githubusersapp.repo

import android.os.Build
import androidx.paging.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.znggis.githubusersapp.mapper.ItemDtoToItemEntity
import com.znggis.githubusersapp.platform.NetworkHandler
import com.znggis.githubusersapp.repo.local.Database
import com.znggis.githubusersapp.repo.local.DefaultLocalDatasource
import com.znggis.githubusersapp.repo.local.LocalDatasource
import com.znggis.githubusersapp.repo.local.entity.ItemEntity
import com.znggis.githubusersapp.repo.local.entity.RemoteKeys
import com.znggis.githubusersapp.repo.model.Query
import com.znggis.githubusersapp.repo.remote.RemoteDatasource
import com.znggis.githubusersapp.util.DbFactory
import com.znggis.githubusersapp.util.LoadItemsDto
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
@Config(sdk = [Build.VERSION_CODES.O_MR1])
@ExperimentalPagingApi
class PageRemoteMediatorTest {

    lateinit var database: Database
    lateinit var localDatasource: LocalDatasource
    var remoteDatasource = mockk<RemoteDatasource>()
    val networkHandler = mockk<NetworkHandler>()
    val mapper = ItemDtoToItemEntity()

    @Before
    fun setup() {
        database = DbFactory.buildDb()
        localDatasource = spyk(DefaultLocalDatasource(database))
    }

    @After
    fun cleanUp() {
        database.close()
        Dispatchers.resetMain()
    }


    @Test
    fun refreshLoad_returnSuccess_whenRemoteDataIsAvailable() = runBlocking {

        val query = Query("Hi")

        every { networkHandler.isNetworkAvailable() } returns true

        val dataRemote = LoadItemsDto.loadFull()

        coEvery { remoteDatasource.search(query.toString(), PAGE_SIZE, 1) }.returns(dataRemote)

        val mediator = PageRemoteMediator(
            query,
            localDatasource,
            remoteDatasource,
            networkHandler
        )


        val pagingState = PagingState<Int, ItemEntity>(
            listOf(),
            null,
            PagingConfig(PAGE_SIZE),
            10
        )

        val result = mediator.load(LoadType.REFRESH, pagingState)

        assertThat(result is RemoteMediator.MediatorResult.Success).isTrue()
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isFalse()

        val localData = mapper.convert(dataRemote.items)

        coVerifyOrder {

            localDatasource.clearRemoteKeys()

            localDatasource.deleteAllItems()

            localDatasource.insertAllKeys(
                dataRemote.items.map {
                    RemoteKeys(
                        itemId = it.id.toLong(),
                        nextKey = 2, prevKey = null
                    )
                })
            localDatasource.insertItems(localData)
        }
    }

    @Test
    fun refreshLoadType_LoadSuccessWithEmptyResult_EndOfPaginationReturned() = runBlocking {
        val query = Query("Hi")

        every { networkHandler.isNetworkAvailable() } returns true

        coEvery { remoteDatasource.search(query.toString(), PAGE_SIZE, 1) }.returns(
            LoadItemsDto.loadEmpty()
        )

        val mediator = PageRemoteMediator(
            query,
            localDatasource,
            remoteDatasource,
            networkHandler
        )

        val pagingState = PagingState<Int, ItemEntity>(
            listOf(),
            null,
            PagingConfig(PAGE_SIZE),
            10
        )

        val result = mediator.load(LoadType.REFRESH, pagingState)

        assertThat(result is RemoteMediator.MediatorResult.Success).isTrue()
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isTrue()
    }

    //TODO add test for Error states

    //TODO add test for no internet connection

    //TODO add test for getRemoteKeyForLastItem

    //TODO add test for getRemoteKeyForFirstItem

    //TODO add test for getRemoteKeyClosestToCurrentPosition

}