package com.josephshawcroft.spacexapi.ui.flightlist

import androidx.lifecycle.LiveData

interface LaunchListViewModel {

    val viewState: LiveData<ViewState>

    fun fetchPageData()

    fun addFilter(filter: LaunchFilter)

    fun clearFilters()

    fun sortLaunchesBy(ascending: Boolean)
}