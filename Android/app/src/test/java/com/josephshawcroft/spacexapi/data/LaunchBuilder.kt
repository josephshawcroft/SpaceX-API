package com.josephshawcroft.spacexapi.data

import com.josephshawcroft.spacexapi.data.model.Launch

class LaunchBuilder(
    var missionName: String = "default name",
    var missionDate: String = "default date",
    var rocketId: String = "1",
    var missionImageUrl: String? = "www.example.com",
    var wasSuccess: Boolean = true
) {

    fun build() = Launch(missionName, missionDate, rocketId, missionImageUrl, wasSuccess)
}