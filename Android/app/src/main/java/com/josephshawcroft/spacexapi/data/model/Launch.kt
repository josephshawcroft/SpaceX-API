package com.josephshawcroft.spacexapi.data.model

data class Launch(
    val missionName: String,
    val missionDate: String,
    val rocketName: String,
    val rocketType: String
) {
    val daysSinceLaunch = Unit // TODO needs to account for both dates before and dates after

}