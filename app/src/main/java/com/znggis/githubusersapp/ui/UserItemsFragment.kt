package com.znggis.githubusersapp.ui


import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.znggis.githubusersapp.R
import com.znggis.githubusersapp.databinding.UserItemsFragmentBinding
import com.znggis.githubusersapp.repo.model.Query
import com.znggis.githubusersapp.ui.adapter.GitHubItemsAdapter
import com.znggis.githubusersapp.ui.adapter.GitItemsLoadStateAdapter
import com.znggis.githubusersapp.ui.adapter.asMergedLoadStates
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@AndroidEntryPoint
class UserItemsFragment : Fragment(R.layout.user_items_fragment) {

    private val binding: UserItemsFragmentBinding by viewBinding(
        UserItemsFragmentBinding::bind
    )


    private val viewModel: UserItemsViewModel by viewModels()


    @Inject
    lateinit var gitHubItemsAdapter: GitHubItemsAdapter

    @Inject
    lateinit var gitItemsLoadStateAdapter: GitItemsLoadStateAdapter

    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initSwipeToRefresh()
        initSearch()
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun initSearch() {
        callbackFlow<String?> {
            binding.tvSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false
                override fun onQueryTextChange(newText: String?): Boolean {
                    offer(newText)
                    return true
                }
            })
            awaitClose()
        }.onEach { newText ->
            viewModel.setQuery(Query(newText))
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }


    private fun initAdapter() {
        binding.list.adapter = gitHubItemsAdapter.withLoadStateHeaderAndFooter(
            header = gitItemsLoadStateAdapter,
            footer = gitItemsLoadStateAdapter
        )

        lifecycleScope.launchWhenCreated {
            gitHubItemsAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.swipeRefresh.isRefreshing =
                    loadStates.mediator?.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.items.collectLatest {
                gitHubItemsAdapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            gitHubItemsAdapter.loadStateFlow
                // Use a state-machine to track LoadStates such that we only transition to
                // NotLoading from a RemoteMediator load if it was also presented to UI.
                .asMergedLoadStates()
                // Only emit when REFRESH changes, as we only want to react on loads replacing the
                // list.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                // Scroll to top is synchronous with UI updates, even if remote load was triggered.
                .collect { binding.list.scrollToPosition(0) }
        }
    }

    private fun initSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener { gitHubItemsAdapter.refresh() }
    }


}