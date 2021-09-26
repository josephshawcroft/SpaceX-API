package com.josephshawcroft.spacexapi.repository

import com.josephshawcroft.spacexapi.data.model.CompanyInfo
import com.josephshawcroft.spacexapi.data.model.Launch
import com.josephshawcroft.spacexapi.data.model.Rocket
import com.josephshawcroft.spacexapi.network.LaunchesService
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface LaunchListRepository {
    fun fetchCompanyInfo(): Single<CompanyInfo>
    fun fetchLaunches(): Single<List<Launch>>
    fun fetchRockets(): Single<List<Rocket>>
}

internal class LaunchListRepositoryImpl @Inject constructor(private val launchesService: LaunchesService) :
    LaunchListRepository {

    override fun fetchCompanyInfo(): Single<CompanyInfo> =
        launchesService.fetchCompany().map { response ->
            CompanyInfo(
                response.employees,
                response.founded,
                response.founder,
                response.launch_sites,
                response.name,
                response.valuation
            )
        }


    override fun fetchLaunches(): Single<List<Launch>> =
        launchesService.fetchLaunches().map { response ->
            response.map {
                Launch(
                    it.name,
                    it.date_utc,
                    it.rocket
                )
            }
        }

    override fun fetchRockets(): Single<List<Rocket>> =
        launchesService.fetchRockets().map { response ->
            response.map {
                Rocket(
                    it.id,
                    it.name,
                    it.type
                )
            }
        }
}