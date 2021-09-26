package com.josephshawcroft.spacexapi.data.api

data class LaunchResponse(
    val date_utc: String,
    val id: String,
    val name: String,
    val rocket: String,
    val success: Boolean,
    val links: Links
)