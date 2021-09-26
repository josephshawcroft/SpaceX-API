package com.josephshawcroft.spacexapi.network

import com.josephshawcroft.spacexapi.data.api.LaunchResponse
import com.josephshawcroft.spacexapi.data.api.RocketResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface LaunchesService {

    @GET("/v4/launches")
    fun fetchLaunches(): Single<List<LaunchResponse>>

    @GET("/v4/rockets")
    fun fetchRockets(): Single<List<RocketResponse>>
}