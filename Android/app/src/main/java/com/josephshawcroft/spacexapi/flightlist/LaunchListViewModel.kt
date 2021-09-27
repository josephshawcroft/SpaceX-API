package com.josephshawcroft.spacexapi.flightlist

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

interface LaunchListViewModel {

    val viewState: LiveData<ViewState>

    fun fetchPageData()

    @VisibleForTesting
    fun fetchCompanyInfo()

    @VisibleForTesting
    fun fetchLaunchList()

    fun setFilters(vararg filters: LaunchFilter)

    fun sortLaunchesBy(ascending: Boolean)
}