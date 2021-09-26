package com.josephshawcroft.spacexapi.network

import com.josephshawcroft.spacexapi.data.api.launchresponse.LaunchResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface LaunchesService {

    @GET("/v4/launches")
    fun fetchLaunches(): Single<List<LaunchResponse>>
}