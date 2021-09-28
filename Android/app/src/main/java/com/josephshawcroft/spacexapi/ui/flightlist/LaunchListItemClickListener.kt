package com.josephshawcroft.spacexapi.ui.flightlist

import com.josephshawcroft.spacexapi.data.model.LaunchWithRocketInfo

interface LaunchListItemClickListener {

    fun onLaunchItemClicked(launch: LaunchWithRocketInfo)
}