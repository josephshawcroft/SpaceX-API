package com.josephshawcroft.spacexapi.ui.flightlist

import com.josephshawcroft.spacexapi.data.model.LaunchWithRocketInfo

sealed class LaunchFilter {
    abstract fun doesItemMatchFilter(item: LaunchWithRocketInfo): Boolean
}

data class YearFilter(val yearsRange: IntRange) : LaunchFilter() {

    override fun doesItemMatchFilter(item: LaunchWithRocketInfo): Boolean {
        return true //todo when made date class
    }
}

data class SuccessFilter(val wasSuccessful: Boolean) : LaunchFilter() {

    override fun doesItemMatchFilter(item: LaunchWithRocketInfo): Boolean {
        return item.launch.wasSuccess == wasSuccessful
    }
}