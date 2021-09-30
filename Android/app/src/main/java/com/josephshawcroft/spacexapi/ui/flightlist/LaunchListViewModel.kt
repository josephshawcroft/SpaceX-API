package com.josephshawcroft.spacexapi.ui.flightlist

import androidx.lifecycle.LiveData

interface LaunchListViewModel {

    val viewState: LiveData<ViewState>

    fun fetchPageData()

    fun setFilters(vararg filters: LaunchFilter)

    fun sortLaunchesBy(ascending: Boolean)
}