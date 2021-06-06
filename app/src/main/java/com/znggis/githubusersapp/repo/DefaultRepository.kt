package com.znggis.githubusersapp.repo

import androidx.paging.*
import com.znggis.githubusersapp.executer.PostExecutionThread
import com.znggis.githubusersapp.platform.NetworkHandler
import com.znggis.githubusersapp.repo.local.LocalDatasource
import com.znggis.githubusersapp.repo.model.GitHubItem
import com.znggis.githubusersapp.repo.model.Query
import com.znggis.githubusersapp.repo.remote.RemoteDatasource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

const val PAGE_SIZE = 30

class DefaultRepository(
    private val localDatasource: LocalDatasource,
    private val remoteDataSource: RemoteDatasource,
    private val dispatcher: PostExecutionThread,
    private val networkHandler: NetworkHandler,
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
            dispatcher,
            networkHandler
        )
    ) {
        localDatasource.getAllItemsPagingSource()
    }.flow.map {
        it.map {
            GitHubItem(
                id = it.id,
                item = it.item,
                avatarUrl = it.avatarUrl,
                score = it.score,
                isFavourite = it.isFavourite
            )
        }
    }


}