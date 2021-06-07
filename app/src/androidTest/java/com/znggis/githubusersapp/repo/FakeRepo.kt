package com.znggis.githubusersapp.repo

import androidx.paging.PagingData
import com.znggis.githubusersapp.repo.model.GitHubItem
import com.znggis.githubusersapp.repo.model.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepo : Repository {

    var items = mutableListOf<GitHubItem>()

    override fun search(query: Query): Flow<PagingData<GitHubItem>> = flow {
        emit(PagingData.from(items))
    }
}