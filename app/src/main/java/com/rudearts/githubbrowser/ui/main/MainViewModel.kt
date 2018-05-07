package com.rudearts.githubbrowser.ui.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.rudearts.githubbrowser.domain.model.RepoItem
import com.rudearts.githubbrowser.domain.usecase.Search4RepoItemsUseCase
import com.rudearts.githubbrowser.extentions.threadToAndroid
import com.rudearts.githubbrowser.model.LoadingState
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val search4RepoItemsUseCase: Search4RepoItemsUseCase) : ViewModel() {

    companion object {
        private const val DEFAULT_QUERY = "a"
        private const val LOAD_DELAY = 1L
    }

    internal val loadingState = MutableLiveData<LoadingState>()
    internal val repoItems = MutableLiveData<List<RepoItem>>()
    internal val errorMessage = MutableLiveData<String>()

    private var previousQuery:String? = null

    private var timedLoadDisposable:Disposable? = null
    private val compositeDisposable = CompositeDisposable()

    fun loadItems(query: String) {
        disposeTimeLoad()
        val preparedQuery = prepareQuery(query)
        if (isQueryRepeated(preparedQuery)) return
        updatePreviousQuery(preparedQuery)

        loadingState.postValue(LoadingState.LOADING)
        compositeDisposable.add(search4RepoItemsUseCase.search(preparedQuery)
                .threadToAndroid()
                .subscribe({ items ->
                    onLoadSuccess(items)
                }, { error ->
                    onLoadError(error) }))
    }

    private fun updatePreviousQuery(currentQuery: String) {
        previousQuery = currentQuery
    }

    private fun isQueryRepeated(currentQuery: String) = when(previousQuery) {
        null -> false
        else -> previousQuery.equals(currentQuery)
    }

    private fun prepareQuery(query: String) = when(query.isNotEmpty()) {
        true -> query
        else -> DEFAULT_QUERY
    }

    private fun onLoadError(error: Throwable?) {
        loadingState.postValue(LoadingState.NO_RESULTS)
        errorMessage.postValue(error?.toString())
    }

    private fun onLoadSuccess(items: List<RepoItem>?) {
        repoItems.postValue(items)
        updateLoadingState(items)
    }

    fun loadItemsDelayed(query:String) {
        disposeTimeLoad()
        timedLoadDisposable = Single.timer(LOAD_DELAY, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe({ _ -> loadItems(query) })

        timedLoadDisposable?.let { disposable ->
            compositeDisposable.add(disposable)
        }
    }

    private fun disposeTimeLoad() {
        timedLoadDisposable?.let { disposable ->
            if (!disposable.isDisposed) {
                disposable.dispose()
            }
        }
    }

    private fun updateLoadingState(items: List<RepoItem>?) {
        when(items?.isNotEmpty()) {
            true -> loadingState.postValue(LoadingState.SHOW_RESULTS)
            else -> loadingState.postValue(LoadingState.NO_RESULTS)
        }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}