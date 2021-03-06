package com.josephshawcroft.spacexapi.data.model

import org.joda.time.DateTime
import org.joda.time.Duration

data class Launch(
    val missionName: String,
    val missionDate: DateTime,
    val rocketId: String,
    val missionImageUrl: String?,
    val articleUrl: String?,
    val wasSuccess: Boolean
) {

    fun daysSinceLaunch(): Long {
        val now = DateTime.now()
        val duration = Duration(missionDate, now)
        return duration.standardDays
    }
}