package com.josephshawcroft.spacexapi.repository

import com.josephshawcroft.spacexapi.data.api.RocketResponse
import com.josephshawcroft.spacexapi.data.model.Launch
import com.josephshawcroft.spacexapi.network.LaunchesService
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface LaunchListRepository {
    fun fetchLaunches(): Single<List<Launch>>
    fun fetchRockets(): Single<List<RocketResponse>>
}

internal class LaunchListRepositoryImpl @Inject constructor(private val launchesService: LaunchesService) :
    LaunchListRepository {

    override fun fetchLaunches(): Single<List<Launch>> =
        launchesService.fetchLaunches().map { response ->
            response.map {
                Launch(
                    it.name,
                    it.date_utc,
                    it.rocket,
                    it.rocket
                )
            }
        }

    override fun fetchRockets(): Single<List<RocketResponse>> =
        launchesService.fetchRockets()
}