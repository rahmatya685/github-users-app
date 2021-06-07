package com.znggis.githubusersapp.repo

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.znggis.githubusersapp.executer.PostExecutionThread
import com.znggis.githubusersapp.mapper.ItemDtoToItemEntity
import com.znggis.githubusersapp.platform.NetworkHandler
import com.znggis.githubusersapp.repo.local.LocalDatasource
import com.znggis.githubusersapp.repo.local.entity.ItemEntity
import com.znggis.githubusersapp.repo.local.entity.RemoteKeys
import com.znggis.githubusersapp.repo.model.Query
import com.znggis.githubusersapp.repo.remote.RemoteDatasource
import com.znggis.githubusersapp.repo.remote.dto.user.UserSearchResp
import retrofit2.HttpException
import java.io.IOException


// GitHub page API is 1 based: https://developer.github.com/v3/#pagination
private const val GITHUB_STARTING_PAGE_INDEX = 1

@ExperimentalPagingApi
class PageRemoteMediator(
    private val query: Query,
    private val localDatasource: LocalDatasource,
    private val remoteDataSource: RemoteDatasource,
    private val networkHandler: NetworkHandler,
    private val itemDtoToItemEntity: ItemDtoToItemEntity =
        ItemDtoToItemEntity()
) : RemoteMediator<Int, ItemEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ItemEntity>
    ): MediatorResult {

        if (!networkHandler.isNetworkAvailable())
            return MediatorResult.Success(endOfPaginationReached = true)

        try {

            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: GITHUB_STARTING_PAGE_INDEX
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    // If remoteKeys is null, that means the refresh result is not in the database yet.
                    // We can return Success with `endOfPaginationReached = false` because Paging
                    // will call this method again if RemoteKeys becomes non-null.
                    // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                    // the end of pagination for prepend.
                    val prevKey = remoteKeys?.prevKey
                    if (prevKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    }
                    prevKey
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    // If remoteKeys is null, that means the refresh result is not in the database yet.
                    // We can return Success with `endOfPaginationReached = false` because Paging
                    // will call this method again if RemoteKeys becomes non-null.
                    // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                    // the end of pagination for append.
                    val nextKey = remoteKeys?.nextKey
                    if (nextKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    }
                    nextKey
                }
            }
            Log.e("Quert=> ", query.toString())

            val data = remoteDataSource.search(
                query.toString(),
                state.config.pageSize,
                page
            )

            val endOfPaginationReached = data.items.isEmpty()

            localDatasource.applyOnDbTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    localDatasource.clearRemoteKeys()
                    localDatasource.deleteAllItems()
                }
                val prevKey = if (page == GITHUB_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (data.items.isEmpty()) null else page + 1
                val keys = data.items.map {
                    RemoteKeys(itemId = it.id.toLong(), prevKey = prevKey, nextKey = nextKey)
                }

                localDatasource.insertAllKeys(keys)

                localDatasource.insertItems(data.items.map(itemDtoToItemEntity::convert))
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }

    }


    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ItemEntity>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { item ->
                // Get the remote keys of the last item retrieved
                localDatasource.remoteKeysItemId(item.itemId.toLong())
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ItemEntity>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { item ->
                // Get the remote keys of the first items retrieved
                localDatasource.remoteKeysItemId(item.itemId.toLong())
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, ItemEntity>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.itemId?.let { itemId ->
                localDatasource.remoteKeysItemId(itemId.toLong())
            }
        }
    }
}