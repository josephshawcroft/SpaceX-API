package com.josephshawcroft.spacexapi.repository

import com.josephshawcroft.spacexapi.data.model.LaunchResponse
import com.josephshawcroft.spacexapi.network.LaunchesService
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface LaunchListRepository {
    fun fetchLaunches() : Single<List<LaunchResponse>>
}

internal class LaunchListRepositoryImpl @Inject constructor(private val launchesService: LaunchesService) :
    LaunchListRepository {

    override fun fetchLaunches() = launchesService.fetchLaunches()
}