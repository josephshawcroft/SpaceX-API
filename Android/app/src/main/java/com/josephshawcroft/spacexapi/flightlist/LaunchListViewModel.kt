package com.josephshawcroft.spacexapi.flightlist

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.josephshawcroft.spacexapi.repository.LaunchListRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

interface LaunchListViewModel {

    fun fetchLaunches()

    companion object {
        fun get(owner: ViewModelStoreOwner): LaunchListViewModel =
            ViewModelProvider(owner).get(LaunchListViewModelImpl::class.java)
    }
}

internal class LaunchListViewModelImpl @ViewModelInject constructor(
    private val repository: LaunchListRepository
) : ViewModel(), LaunchListViewModel {

    private var disposable: CompositeDisposable? = null

    override fun fetchLaunches() {
        repository.fetchLaunches()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                Log.d("test", list.toString())
            }
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}