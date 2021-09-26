package com.josephshawcroft.spacexapi.flightlist

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.josephshawcroft.spacexapi.data.Response
import com.josephshawcroft.spacexapi.data.model.Launch
import com.josephshawcroft.spacexapi.data.model.LaunchWithRocketInfo
import com.josephshawcroft.spacexapi.data.model.Rocket
import com.josephshawcroft.spacexapi.repository.LaunchListRepository
import com.josephshawcroft.spacexapi.utils.ioToMainScheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction

interface LaunchListViewModel {

    val launches: LiveData<Response<LaunchWithRocketInfo>>

    fun fetchLaunchList()

    companion object {
        fun get(owner: ViewModelStoreOwner): LaunchListViewModel =
            ViewModelProvider(owner).get(LaunchListViewModelImpl::class.java)
    }
}

internal class LaunchListViewModelImpl @ViewModelInject constructor(
    private val repository: LaunchListRepository
) : ViewModel(), LaunchListViewModel {

    private var compositeDisposable = CompositeDisposable()
    private val _launches = MutableLiveData<Response<LaunchWithRocketInfo>>(Response.NotYetLoaded())
    override val launches: LiveData<Response<LaunchWithRocketInfo>>
        get() = _launches

    override fun fetchLaunchList() {
        Single.zip<List<Launch>, List<Rocket>, List<LaunchWithRocketInfo>>(
            fetchLaunches(),
            fetchRockets(),
            BiFunction { launches, rockets ->
                launches.map { launch ->
                    val rocket = rockets.first { launch.rocketName == it.id }
                    LaunchWithRocketInfo(launch, rocket)
                }
            })
            .ioToMainScheduler()
            .subscribe({

            }, {

            })

    }

    fun fetchLaunch() {
        repository.fetchLaunches()
            .ioToMainScheduler()
            .subscribe({ list ->
                Log.d("test", list.toString())
            }, {
                Log.e("test", it.message.toString())
            }).addToDisposables()
    }

    fun fetchRocket() {
        repository.fetchRockets()
            .ioToMainScheduler()
            .subscribe({ list ->
                Log.d("test", list.toString())
            }, {
                Log.e("test", it.message.toString())
            }).addToDisposables()
    }

    private fun fetchLaunches() = repository.fetchLaunches()
    private fun fetchRockets() = repository.fetchRockets()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    private fun Disposable.addToDisposables() = compositeDisposable.add(this)
}