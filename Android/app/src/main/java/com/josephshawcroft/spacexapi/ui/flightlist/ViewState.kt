package com.josephshawcroft.spacexapi.ui.flightlist

import com.josephshawcroft.spacexapi.data.model.CompanyInfo

sealed class ViewState {
    object Loading : ViewState()
    object Error : ViewState()
    data class Loaded(val companyInfo: CompanyInfo, val launchesList: LaunchesList) : ViewState()
}