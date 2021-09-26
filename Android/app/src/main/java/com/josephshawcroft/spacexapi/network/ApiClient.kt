package com.josephshawcroft.spacexapi.network

import retrofit2.Retrofit
import javax.inject.Inject

class ApiClient @Inject constructor(retrofit: Retrofit) {

    private val launchesService: LaunchesService =
        retrofit.create(LaunchesService::class.java)
}