package com.josephshawcroft.spacexapi.ui.flightlist

import com.josephshawcroft.spacexapi.data.model.LaunchWithRocketInfo
import org.joda.time.DateTime

sealed class LaunchFilter {
    abstract fun doesItemMatchFilter(item: LaunchWithRocketInfo): Boolean
}

data class YearFilter(val yearsRange: Pair<DateTime, DateTime>) : LaunchFilter() {

    override fun doesItemMatchFilter(item: LaunchWithRocketInfo): Boolean =
        item.launch.missionDate.isAfter(yearsRange.first) &&
                item.launch.missionDate.isBefore(yearsRange.second)
}

data class SuccessFilter(val wasSuccessful: Boolean) : LaunchFilter() {

    override fun doesItemMatchFilter(item: LaunchWithRocketInfo): Boolean {
        return item.launch.wasSuccess == wasSuccessful
    }
}