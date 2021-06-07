package com.znggis.githubusersapp.repo

import androidx.paging.PagingData
import com.znggis.githubusersapp.repo.model.GitHubItem
import com.znggis.githubusersapp.repo.model.Query
import kotlinx.coroutines.flow.Flow

/**
 * Abstraction for repository of entities
 */
interface Repository {
    fun search(query: Query): Flow<PagingData<GitHubItem>>
    suspend fun toggleItemFavourite(item: GitHubItem)
    fun getItemInfo(it: Int): Flow<GitHubItem?>
}