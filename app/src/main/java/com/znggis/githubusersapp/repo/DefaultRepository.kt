package com.znggis.githubusersapp.repo

import androidx.paging.*
import com.znggis.githubusersapp.executer.PostExecutionThread
import com.znggis.githubusersapp.mapper.ItemEntityToGitHubItem
import com.znggis.githubusersapp.platform.NetworkHandler
import com.znggis.githubusersapp.repo.local.LocalDatasource
import com.znggis.githubusersapp.repo.model.GitHubItem
import com.znggis.githubusersapp.repo.model.Query
import com.znggis.githubusersapp.repo.remote.RemoteDatasource
import kotlinx.coroutines.flow.*
import javax.inject.Inject

const val PAGE_SIZE = 100

class DefaultRepository
@Inject constructor(
    private val localDatasource: LocalDatasource,
    private val remoteDataSource: RemoteDatasource,
    private val networkHandler: NetworkHandler,
    private val executionThread: PostExecutionThread,
    private val itemEntityToGitHubItem: ItemEntityToGitHubItem =
        ItemEntityToGitHubItem()
) : Repository {


    @ExperimentalPagingApi
    override fun search(query: Query) = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
        ),
        remoteMediator = PageRemoteMediator(
            query,
            localDatasource,
            remoteDataSource,
            networkHandler
        )
    ) {
        localDatasource.getAllItemsPagingSource()
    }.flow.map {
        it.map(itemEntityToGitHubItem::convert)
    }


    override suspend fun toggleItemFavourite(item: GitHubItem) =
        localDatasource.toggleItemFav(item.id)
//
//    override fun getItemInfo(itemId: Int): GitHubItem? =
//        localDatasource.findItem(itemId)
//            ?.let(itemEntityToGitHubItem::convert)
//
//    override fun getItemInfo(itemId: Int): Flow<GitHubItem?> = flow {
//        val itemModel =
//            ?.let(itemEntityToGitHubItem::convert)
//        emit(itemModel)
//    }.flowOn(executionThread.io)


    override fun getItemInfo(itemId: Int): Flow<GitHubItem?> =
        localDatasource.findItem(itemId).mapNotNull {
            it?.let {
                itemEntityToGitHubItem.convert(it)
            }
        }.flowOn(executionThread.io)
}