package com.znggis.githubusersapp.repo

import androidx.paging.PagingData
import com.znggis.githubusersapp.repo.model.GitHubItem
import com.znggis.githubusersapp.repo.model.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeRepo : Repository {

    var items = mutableListOf<GitHubItem>()

    override fun search(query: Query): Flow<PagingData<GitHubItem>> = flow {
        emit(PagingData.from(items))
    }

    override suspend fun toggleItemFavourite(item: GitHubItem) {

    }

    override fun getItemInfo(idLocal: Int): Flow<GitHubItem?> =
        flowOf(items.find { it.id == idLocal})
}