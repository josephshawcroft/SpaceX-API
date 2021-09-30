package com.josephshawcroft.spacexapi.ui.flightlist

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.josephshawcroft.spacexapi.data.Response
import com.josephshawcroft.spacexapi.data.Response.*
import com.josephshawcroft.spacexapi.data.model.CompanyInfo
import com.josephshawcroft.spacexapi.data.model.Launch
import com.josephshawcroft.spacexapi.data.model.LaunchWithRocketInfo
import com.josephshawcroft.spacexapi.data.model.Rocket
import com.josephshawcroft.spacexapi.repository.SpaceXRepository
import com.josephshawcroft.spacexapi.utils.CombinedLiveData
import com.josephshawcroft.spacexapi.utils.applyIoToMainObservable
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction
import javax.inject.Inject

typealias LaunchesList = List<LaunchWithRocketInfo>

@HiltViewModel
internal class LaunchListViewModelImpl @Inject constructor(
    private val repository: SpaceXRepository,
    @VisibleForTesting val compositeDisposable: CompositeDisposable
) : ViewModel(), LaunchListViewModel {

    private val _launches = MutableLiveData<Response<LaunchesList>>()
    private val _companyInfo = MutableLiveData<Response<CompanyInfo>>()

    @VisibleForTesting
    val launches: LiveData<Response<LaunchesList>> = _launches

    @VisibleForTesting
    val companyInfo: LiveData<Response<CompanyInfo>> = _companyInfo

    private val _filters = MutableLiveData<List<LaunchFilter>>(emptyList())
    private val _filteredLaunches =
        CombinedLiveData<List<LaunchFilter>, Response<LaunchesList>, Response<LaunchesList>>(
            _filters,
            _launches
        ) { filters, launches ->
            if (launches !is Success) launches ?: IsLoading()
            else {
                val filteredLaunches = launches.data.toMutableList()
                filters?.forEach { filter ->
                    filteredLaunches.removeIf { !filter.doesItemMatchFilter(it) }
                }
                Success(filteredLaunches.toList())
            }
        }

    override val viewState: LiveData<ViewState> =
        CombinedLiveData<Response<LaunchesList>, Response<CompanyInfo>, ViewState>(
            _filteredLaunches,
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

    @VisibleForTesting
    fun fetchCompanyInfo() {
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

    @VisibleForTesting
    fun fetchLaunchList() {
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

    override fun setFilters(vararg filters: LaunchFilter) {
        _filters.value = listOf(*filters)
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