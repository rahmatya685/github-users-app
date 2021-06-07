package com.znggis.githubusersapp.ui

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.znggis.githubusersapp.executer.PostExecutionThread
import com.znggis.githubusersapp.repo.Repository
import com.znggis.githubusersapp.repo.model.GitHubItem
import com.znggis.githubusersapp.repo.model.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserItemsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: Repository,
    private val postExecutionThread: PostExecutionThread
) : ViewModel() {

    var lastFirstVisiblePosition: Int? = null

    companion object {
        const val QUERY_KEY = "QUERY_KEY"
        const val DEFAULT_QUERY = "sobrin"
        const val SELECTED_ITEM_ID = "SELECTED_ITEM_ID"
    }

    init {
        if (!savedStateHandle.contains(QUERY_KEY)) {
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


    @FlowPreview
    val selectedItem: LiveData<GitHubItem?> =
        savedStateHandle.getLiveData<Int>(SELECTED_ITEM_ID)
            .switchMap { id ->
                repository.getItemInfo(id).asLiveData()
            }


    private fun queryIsValid(
        query: Query
    ) = savedStateHandle.getLiveData<Query>(QUERY_KEY)
        .value.toString() != query.toString()
            && query.isValid()

    fun setQuery(query: Query) {

        if (!queryIsValid(query)) return

        clearListCh.offer(Unit)

        savedStateHandle.set(QUERY_KEY, query)
    }

    fun toggleItemFavourite(item: GitHubItem) {
        viewModelScope.launch(postExecutionThread.io) {
            repository.toggleItemFavourite(item)
        }
    }

    fun setSelectedItem(item: GitHubItem) {
        savedStateHandle[SELECTED_ITEM_ID] = item.id
    }

    fun setScrollPosition(lastFirstVisiblePosition: Int) {
        this.lastFirstVisiblePosition = lastFirstVisiblePosition
    }


}