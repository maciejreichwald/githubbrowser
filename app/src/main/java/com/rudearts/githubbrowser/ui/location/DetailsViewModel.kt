package com.rudearts.githubbrowser.ui.location

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.rudearts.githubbrowser.domain.model.User
import com.rudearts.githubbrowser.domain.usecase.LoadUserUseCase
import com.rudearts.githubbrowser.extentions.threadToAndroid
import com.rudearts.githubbrowser.model.LoadingState
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
        private val loadUserUseCase: LoadUserUseCase) : ViewModel() {

    internal val user = MutableLiveData<User>()
    internal val errorMessage = MutableLiveData<String>()
    internal val loadingState = MutableLiveData<LoadingState>()

    private val compositeDisposable = CompositeDisposable()

    fun loadUser(name: String) {
        loadingState.postValue(LoadingState.LOADING)
        compositeDisposable.add(loadUserUseCase.loadUser(name)
                .threadToAndroid()
                .subscribe({ loadedUser ->
                    loadingState.postValue(LoadingState.SHOW_RESULTS)
                    user.postValue(loadedUser)
                }, { error ->
                    loadingState.postValue(LoadingState.ERROR)
                    errorMessage.postValue(error.toString())
                })
        )
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

}