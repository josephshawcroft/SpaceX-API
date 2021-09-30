package com.josephshawcroft.spacexapi.data

import com.josephshawcroft.spacexapi.data.model.Launch
import org.joda.time.DateTime

class LaunchBuilder(
    var missionName: String = "default name",
    var missionDate: DateTime = DateTime(),
    var rocketId: String = "1",
    var missionImageUrl: String? = "www.example.com",
    var articleUrl: String? = "www.example.com",
    var wasSuccess: Boolean = true
) {

    fun build() =
        Launch(missionName, missionDate, rocketId, missionImageUrl, articleUrl, wasSuccess)
}