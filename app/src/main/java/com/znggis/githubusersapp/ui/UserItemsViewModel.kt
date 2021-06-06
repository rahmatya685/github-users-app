package com.znggis.githubusersapp.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.znggis.githubusersapp.BuildConfig
import com.znggis.githubusersapp.executer.PostExecutionThread
import com.znggis.githubusersapp.repo.Repository
import com.znggis.githubusersapp.repo.model.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class UserItemsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: Repository,
    private val postExecutionThread: PostExecutionThread
) : ViewModel() {

    companion object {
        const val QUERY_KEY = "QUERY_KEY"
        const val DEFAULT_QUERY = "sobrin"
    }

    init {
        if (!savedStateHandle.contains(QUERY_KEY) && BuildConfig.DEBUG) {
            savedStateHandle.set(QUERY_KEY, Query(DEFAULT_QUERY))
        }
    }

    private val clearListCh = Channel<Unit>(Channel.CONFLATED)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val items = flowOf(
        clearListCh.receiveAsFlow().map { PagingData.empty() },
        savedStateHandle.getLiveData<Query>(QUERY_KEY)
            .asFlow()
            .flatMapLatest { repository.search(it) }
            // cachedIn() shares the paging state across multiple consumers of posts,
            // e.g. different generations of UI across rotation config change
            .cachedIn(viewModelScope)
    ).flattenMerge(2)

    private fun queryIsValid(
        query: Query
    ) = savedStateHandle.get<Query>(QUERY_KEY) != query
            && query.isValid()

    fun setQuery(query: Query) {

        if (!queryIsValid(query)) return

        clearListCh.offer(Unit)

        savedStateHandle.set(QUERY_KEY, query)
    }

}