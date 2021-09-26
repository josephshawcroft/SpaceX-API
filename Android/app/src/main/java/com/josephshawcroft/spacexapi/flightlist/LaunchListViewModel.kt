package com.josephshawcroft.spacexapi.flightlist

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.josephshawcroft.spacexapi.repository.LaunchListRepository
import com.josephshawcroft.spacexapi.utils.ioToMainScheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

interface LaunchListViewModel {

    fun fetchLaunches()

    fun fetchRockets()

    companion object {
        fun get(owner: ViewModelStoreOwner): LaunchListViewModel =
            ViewModelProvider(owner).get(LaunchListViewModelImpl::class.java)
    }
}

internal class LaunchListViewModelImpl @ViewModelInject constructor(
    private val repository: LaunchListRepository
) : ViewModel(), LaunchListViewModel {

    private var compositeDisposable = CompositeDisposable()

    override fun fetchLaunches() {
        repository.fetchLaunches()
            .ioToMainScheduler()
            .subscribe({ list ->
                Log.d("test", list.toString())
            }, {
                Log.e("test", it.message.toString())
            }).addToDisposables()
    }

    override fun fetchRockets() {
        repository.fetchRockets()
            .ioToMainScheduler()
            .subscribe({ list ->
                Log.d("test", list.toString())
            }, {
                Log.e("test", it.message.toString())
            }).addToDisposables()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    private fun Disposable.addToDisposables() = compositeDisposable.add(this)
}