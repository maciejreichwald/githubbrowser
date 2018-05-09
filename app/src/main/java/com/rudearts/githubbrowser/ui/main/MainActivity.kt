package com.rudearts.githubbrowser.ui.main

import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.rudearts.githubbrowser.R
import com.rudearts.githubbrowser.domain.model.RepoItem
import com.rudearts.githubbrowser.domain.model.RepoItemType
import com.rudearts.githubbrowser.extentions.*
import com.rudearts.githubbrowser.model.LoadingState
import com.rudearts.githubbrowser.ui.ToolbarActivity
import com.rudearts.githubbrowser.ui.location.DetailsActivity
import javax.inject.Inject


/**
 * Yes, it is open, you can see in MainActivityTest bottom comment why
 */
open class MainActivity : ToolbarActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var viewModel: MainViewModel

    @Inject
    internal lateinit var adapter: RepoItemAdapter

    internal val refreshLayout: SwipeRefreshLayout by bind(R.id.swipe_container)
    internal val progressBar: View by bind(R.id.progress_bar)
    internal val listItems: RecyclerView by bind(R.id.items_list)
    internal val emptyView: View by bind(R.id.empty_view)

    override fun provideSubContentViewId() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
    }

    internal fun setup() {
        inject()
        setupTitle()
        setupSearchView()
        setupRefresh()
        setupList()
        setupViewModel()
        loadItemsUsingQuery()
    }

    internal fun setupViewModel() {
        viewModel = getViewModel<MainViewModel>(viewModelFactory).apply {
            observe(repoItems, ::updateItems)
            observe(loadingState, ::onLoadingStateChange)
            observe(errorMessage, ::onErrorMessage)
        }
    }

    private fun onErrorMessage(message: String?) {
        message?.let {
            showSnackMessage(it)
        }
    }

    internal fun onLoadingStateChange(loadingState: LoadingState?) {
        progressBar.visible = loadingState == LoadingState.LOADING
        emptyView.visible = loadingState == LoadingState.NO_RESULTS
        listItems.visible = loadingState == LoadingState.SHOW_RESULTS
    }

    internal fun updateItems(items: List<RepoItem>?) {
        when (items) {
            null -> adapter.updateItems(emptyList())
            else -> adapter.updateItems(items)
        }
    }

    internal fun setupList() {
        adapter.listener = { item -> onRepoItemClick(item) }
        listItems.adapter = adapter
        listItems.layoutManager = LinearLayoutManager(this)
    }

    private fun onRepoItemClick(item: RepoItem?) {
        if (item?.type == RepoItemType.REPOSITORY) return

        item?.let { repoItem ->
            Intent(this@MainActivity, DetailsActivity::class.java).apply {
                putExtra(DetailsActivity.USER_NAME_PARAM, repoItem.name)
                startActivity(this@apply)
            }
        }

    }

    internal fun setupRefresh() {
        refreshLayout.setOnRefreshListener { onRefresh() }
    }

    internal fun onRefresh() {
        refreshLayout.isRefreshing = false
        loadItemsUsingQuery()
    }

    private fun loadItemsUsingQuery() {
        viewModel.loadItems(searchView.query.toString())
    }

    private fun loadDelayedItemUsingQuery() {
        viewModel.loadItemsDelayed(searchView.query.toString())
    }

    internal fun setupSearchView() {
        showSearchView()
        with(searchView) {
            setOnQueryTextListener(createOnQueryTextListener())
            setOnCloseListener {
                loadItemsUsingQuery()
                false
            }
        }
    }

    internal fun createOnQueryTextListener() = object : OnQueryTextListener() {
        override fun onQueryTextChange(newText: String?): Boolean {
            loadDelayedItemUsingQuery()
            return super.onQueryTextChange(newText)
        }

        override fun onQueryTextSubmit(query: String?): Boolean {
            loadItemsUsingQuery()
            return super.onQueryTextSubmit(query)
        }
    }


    internal fun inject() {
        getAppInjector().inject(this)
    }

    internal fun setupTitle() {
        title = getString(R.string.app_name)
    }

    override fun onBackPressed() = with(searchView) {
        when (isIconified) {
            true -> super.onBackPressed()
            false -> isIconified = true
        }
    }
}
