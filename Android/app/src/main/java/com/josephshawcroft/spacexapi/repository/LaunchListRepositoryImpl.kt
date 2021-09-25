package com.josephshawcroft.spacexapi.repository

import com.josephshawcroft.spacexapi.network.ApiClient
import io.reactivex.Single
import javax.inject.Inject

interface LaunchListRepository {
    fun fetchLaunches()
}

internal class LaunchListRepositoryImpl @Inject constructor(private val apiClient: ApiClient) :
    LaunchListRepository {

    override fun fetchLaunches() = TODO()
}