package com.josephshawcroft.spacexapi.network

import io.reactivex.Single
import retrofit2.Retrofit
import javax.inject.Inject

class ApiClient @Inject constructor(retrofit: Retrofit) {

    private val launchesService: LaunchesService =
        retrofit.create(LaunchesService::class.java)
}