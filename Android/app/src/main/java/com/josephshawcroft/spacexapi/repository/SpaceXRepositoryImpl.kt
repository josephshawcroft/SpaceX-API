package com.josephshawcroft.spacexapi.repository

import com.josephshawcroft.spacexapi.data.model.CompanyInfo
import com.josephshawcroft.spacexapi.data.model.Launch
import com.josephshawcroft.spacexapi.data.model.Rocket
import com.josephshawcroft.spacexapi.network.SpaceXApiService
import io.reactivex.rxjava3.core.Single
import org.joda.time.DateTime
import javax.inject.Inject

interface SpaceXRepository {
    fun fetchCompanyInfo(): Single<CompanyInfo>
    fun fetchLaunches(): Single<List<Launch>>
    fun fetchRockets(): Single<List<Rocket>>
}

internal class SpaceXRepositoryImpl @Inject constructor(private val spaceXApiService: SpaceXApiService) :
    SpaceXRepository {

    override fun fetchCompanyInfo(): Single<CompanyInfo> =
        spaceXApiService.fetchCompany().map { response ->
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
        spaceXApiService.fetchLaunches().map { response ->
            response.map {
                Launch(
                    it.name,
                    DateTime.parse(it.date_utc),
                    it.rocket,
                    it.links.patch.small,
                    it.links.article,
                    it.success
                )
            }
        }

    override fun fetchRockets(): Single<List<Rocket>> =
        spaceXApiService.fetchRockets().map { response ->
            response.map {
                Rocket(
                    it.id,
                    it.name,
                    it.type
                )
            }
        }
}