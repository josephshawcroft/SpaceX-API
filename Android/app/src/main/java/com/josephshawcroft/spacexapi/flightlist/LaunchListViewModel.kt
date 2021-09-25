package com.josephshawcroft.spacexapi.flightlist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.josephshawcroft.spacexapi.repository.LaunchListRepository
import io.reactivex.disposables.Disposable

interface LaunchListViewModel {

    companion object {
        fun get(owner: ViewModelStoreOwner): LaunchListViewModel =
            ViewModelProvider(owner).get(LaunchListViewModelImpl::class.java)
    }
}

internal class LaunchListViewModelImpl @ViewModelInject constructor(
    private val repository: LaunchListRepository
) : ViewModel(), LaunchListViewModel {

    private var disposable: Disposable? = null

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}