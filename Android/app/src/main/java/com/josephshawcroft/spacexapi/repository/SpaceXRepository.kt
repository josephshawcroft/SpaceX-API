package com.josephshawcroft.spacexapi.repository

import com.josephshawcroft.spacexapi.data.model.CompanyInfo
import com.josephshawcroft.spacexapi.data.model.Launch
import com.josephshawcroft.spacexapi.data.model.Rocket
import io.reactivex.rxjava3.core.Single

interface SpaceXRepository {
    fun fetchCompanyInfo(): Single<CompanyInfo>
    fun fetchLaunches(): Single<List<Launch>>
    fun fetchRockets(): Single<List<Rocket>>
}
