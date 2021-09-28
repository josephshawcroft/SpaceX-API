package com.josephshawcroft.spacexapi.ui.flightlist

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData

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