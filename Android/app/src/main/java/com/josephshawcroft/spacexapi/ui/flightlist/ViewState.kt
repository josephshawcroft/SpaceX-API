package com.josephshawcroft.spacexapi.ui.flightlist

import com.josephshawcroft.spacexapi.data.model.CompanyInfo
import com.josephshawcroft.spacexapi.data.model.LaunchWithRocketInfo

sealed class ViewState {
    object Loading : ViewState()
    object Error : ViewState()
    data class Loaded(val companyInfo: CompanyInfo, val launches: List<LaunchWithRocketInfo>) : ViewState()
}