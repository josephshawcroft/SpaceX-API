package com.josephshawcroft.spacexapi.flightlist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.josephshawcroft.spacexapi.data.Response
import com.josephshawcroft.spacexapi.data.Response.*
import com.josephshawcroft.spacexapi.data.model.CompanyInfo
import com.josephshawcroft.spacexapi.data.model.Launch
import com.josephshawcroft.spacexapi.data.model.LaunchWithRocketInfo
import com.josephshawcroft.spacexapi.data.model.Rocket
import com.josephshawcroft.spacexapi.repository.LaunchListRepository
import com.josephshawcroft.spacexapi.utils.ioToMainScheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction

typealias LaunchesList = List<LaunchWithRocketInfo>

interface LaunchListViewModel {

    val launches: LiveData<Response<LaunchesList>>

    fun fetchCompanyInfo()
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
    private val _launches = MutableLiveData<Response<LaunchesList>>(NotYetLoaded())
    override val launches: LiveData<Response<LaunchesList>>
        get() = _launches

    private val _companyInfo = MutableLiveData<Response<CompanyInfo>>(NotYetLoaded())

    override fun fetchCompanyInfo() {
        repository.fetchCompanyInfo()
            .ioToMainScheduler()
            .doOnSubscribe { _companyInfo.value = IsLoading() }
            .subscribe({
                _companyInfo.postValue(Success(it))
            }, {
                _companyInfo.postValue(Error(it))
            })
    }

    override fun fetchLaunchList() {
        Single.zip<List<Launch>, List<Rocket>, LaunchesList>(
            repository.fetchLaunches(),
            repository.fetchRockets(),
            BiFunction { launches, rockets ->
                launches.map { launch ->
                    val rocket = rockets.first { launch.rocketId == it.id } //TODO
                    LaunchWithRocketInfo(launch, rocket)
                }
            })
            .ioToMainScheduler()
            .doOnSubscribe { _launches.value = IsLoading() }
            .subscribe({
                _launches.postValue(Success(it))
            }, {
                _launches.postValue(Error(it.cause))
            })
            .addToDisposables()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    private fun Disposable.addToDisposables() = compositeDisposable.add(this)
}