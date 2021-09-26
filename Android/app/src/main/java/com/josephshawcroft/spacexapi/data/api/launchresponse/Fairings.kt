package com.josephshawcroft.spacexapi.data.api.launchresponse

data class Fairings(
    val recovered: Boolean,
    val recovery_attempt: Boolean,
    val reused: Boolean,
    val ships: List<Any>
)