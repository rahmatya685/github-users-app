package com.znggis.githubusersapp.ui


import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
 import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.znggis.githubusersapp.R
import com.znggis.githubusersapp.databinding.UserItemsFragmentBinding
import com.znggis.githubusersapp.ui.adapter.GitHubItemsAdapter
import com.znggis.githubusersapp.ui.adapter.GitItemsLoadStateAdapter
import com.znggis.githubusersapp.ui.adapter.asMergedLoadStates
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter

class UserItemsFragment : Fragment(R.layout.user_items_fragment) {

    private val binding: UserItemsFragmentBinding by viewBinding(
        UserItemsFragmentBinding::bind
    )

    private val viewModel by viewModels<UserItemsViewModel> { getViewModelsFactory() }

    private lateinit var adapter: GitHubItemsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }


    private fun initAdapter() {
        adapter = GitHubItemsAdapter()
        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = GitItemsLoadStateAdapter(adapter),
            footer = GitItemsLoadStateAdapter(adapter)
        )

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.swipeRefresh.isRefreshing = loadStates.mediator?.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.items.collectLatest {
                adapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow
                // Use a state-machine to track LoadStates such that we only transition to
                // NotLoading from a RemoteMediator load if it was also presented to UI.
                .asMergedLoadStates()
                // Only emit when REFRESH changes, as we only want to react on loads replacing the
                // list.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                // Scroll to top is synchronous with UI updates, even if remote load was triggered.
                .collect {  binding.list.scrollToPosition(0) }
        }
    }

    private fun initSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener { adapter.refresh() }
    }




}