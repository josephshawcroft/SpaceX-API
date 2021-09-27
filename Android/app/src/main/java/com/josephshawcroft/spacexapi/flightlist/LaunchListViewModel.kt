package com.josephshawcroft.spacexapi.flightlist

import androidx.annotation.VisibleForTesting
import androidx.fragment.app.FragmentActivity
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.josephshawcroft.spacexapi.data.Response
import com.josephshawcroft.spacexapi.data.Response.*
import com.josephshawcroft.spacexapi.data.model.CompanyInfo
import com.josephshawcroft.spacexapi.data.model.Launch
import com.josephshawcroft.spacexapi.data.model.LaunchWithRocketInfo
import com.josephshawcroft.spacexapi.data.model.Rocket
import com.josephshawcroft.spacexapi.repository.SpaceXRepository
import com.josephshawcroft.spacexapi.utils.CombinedLiveData
import com.josephshawcroft.spacexapi.utils.applyIoToMainObservable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction

typealias LaunchesList = List<LaunchWithRocketInfo>

interface LaunchListViewModel {

    val viewState: LiveData<ViewState>

    fun fetchPageData()

    @VisibleForTesting
    fun fetchCompanyInfo()

    @VisibleForTesting
    fun fetchLaunchList()

    fun sortLaunchesBy(ascending: Boolean)

    companion object {
        fun get(fragmentActivity: FragmentActivity): LaunchListViewModel =
            ViewModelProvider(fragmentActivity).get(LaunchListViewModelImpl::class.java)
    }
}

internal class LaunchListViewModelImpl @ViewModelInject constructor(
    private val repository: SpaceXRepository,
    @VisibleForTesting val compositeDisposable: CompositeDisposable
) : ViewModel(), LaunchListViewModel {

    private val _launches = MutableLiveData<Response<LaunchesList>>()

    @VisibleForTesting
    val launches
        get() = _launches

    private val _companyInfo = MutableLiveData<Response<CompanyInfo>>()

    @VisibleForTesting
    val companyInfo
        get() = _companyInfo

    override val viewState: LiveData<ViewState> =
        CombinedLiveData<Response<LaunchesList>, Response<CompanyInfo>, ViewState>(
            _launches,
            _companyInfo
        ) { launches, company ->
            when {
                launches is IsLoading || company is IsLoading -> ViewState.Loading
                launches is Error || company is Error -> ViewState.Error
                launches is Success && company is Success -> ViewState.Loaded(
                    company.data,
                    launches.data
                )
                else -> ViewState.Loading
            }
        }

    override fun fetchPageData() {
        fetchCompanyInfo()
        fetchLaunchList()
    }

    override fun fetchCompanyInfo() {
        repository.fetchCompanyInfo()
            .applyIoToMainObservable()
            .doOnSubscribe { _companyInfo.value = IsLoading() }
            .subscribe({
                _companyInfo.postValue(Success(it))
            }, {
                _companyInfo.postValue(Error(it))
            })
            .addToDisposables()
    }

    override fun fetchLaunchList() {
        Single.zip<List<Launch>, List<Rocket>, LaunchesList>(
            repository.fetchLaunches(),
            repository.fetchRockets(),
            BiFunction { launches, rockets ->
                launches.map { launch ->
                    val rocket = rockets.first { launch.rocketId == it.id }
                    LaunchWithRocketInfo(launch, rocket)
                }
            })
            .applyIoToMainObservable()
            .doOnSubscribe { _launches.value = IsLoading() }
            .subscribe({
                _launches.postValue(Success(it))
            }, {
                _launches.postValue(Error(it.cause))
            })
            .addToDisposables()
    }

    override fun sortLaunchesBy(ascending: Boolean) {
        val launches = _launches.value
        if (launches is Success) {
            val sortedLaunches = if (ascending) {
                launches.data.sortedBy { it.launch.missionName }
            } else {
                launches.data.sortedByDescending { it.launch.missionName }
            }
            _launches.value = Success(sortedLaunches)
        }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    private fun Disposable.addToDisposables() = compositeDisposable.add(this)
}