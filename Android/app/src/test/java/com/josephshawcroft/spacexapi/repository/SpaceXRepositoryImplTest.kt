package com.josephshawcroft.spacexapi.repository

import com.josephshawcroft.spacexapi.data.api.*
import com.josephshawcroft.spacexapi.network.SpaceXApiService
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import org.assertj.core.api.Assertions.assertThat
import org.joda.time.DateTime
import org.junit.Test

class SpaceXRepositoryImplTest {

    private var spaceXApiService: SpaceXApiService = mockk()
    private var spaceXRepository = SpaceXRepositoryImpl(spaceXApiService)

    @Test
    fun givenRepository_whenFetchCompanyCalled_thenCompanyInfoCreatedAndPropertiesCorrect() {

        val companyInfoResponse = CompanyInfoResponse(
            employees = 20,
            founded = 1970,
            founder = "Ronald McDonald",
            launch_sites = 9,
            name = "Woolworths",
            valuation = 9405L
        )

        every { spaceXApiService.fetchCompany() } returns Single.just(companyInfoResponse)

        val companyInfo = spaceXRepository.fetchCompanyInfo().blockingGet()

        with(companyInfo) {
            assertThat(employees).isEqualTo(companyInfoResponse.employees)
            assertThat(founded).isEqualTo(companyInfoResponse.founded)
            assertThat(founder).isEqualTo(companyInfoResponse.founder)
            assertThat(launchSites).isEqualTo(companyInfoResponse.launch_sites)
            assertThat(name).isEqualTo(companyInfoResponse.name)
            assertThat(valuation).isEqualTo(companyInfoResponse.valuation)
        }
    }

    @Test
    fun givenRepository_whenFetchLaunchesCalled_thenLaunchObjectsCreatedAndPropertiesCorrect() {

        val launchesResponse = listOf(
            LaunchResponse(
                date_utc = "2007-03-21T01:10:00.000Z",
                id = "5eb87cdaffd86e000604b32b",
                name = "Mega mission",
                rocket = "2gfdusnak",
                success = true,
                links = Links(Patch("www.example.com"), "www.example.com")
            ),
            LaunchResponse(
                date_utc = "2017-03-31T01:10:00.000Z",
                id = "42udnaffd86e000604b32b",
                name = "Sad mission",
                rocket = "id_2tndi9",
                success = false,
                links = Links(Patch("www.example.com"), "www.example.com")
            ),
            LaunchResponse(
                date_utc = "2011-11-11T01:10:00.000Z",
                id = "y2mclamcbvoa",
                name = "Skyrim mission",
                rocket = "isssssss_2tndi9",
                success = true,
                links = Links(Patch("www.example.com"), "www.example.com")
            )
        )

        every { spaceXApiService.fetchLaunches() } returns Single.just(launchesResponse)

        val launches = spaceXRepository.fetchLaunches().blockingGet()
        launches.forEachIndexed { index, launch ->
            val launchResponse = launchesResponse[index]
            with(launch) {
                assertThat(missionName).isEqualTo(launchResponse.name)
                assert(missionDate.isEqual(DateTime(launchResponse.date_utc)))
                assertThat(rocketId).isEqualTo(launchResponse.rocket)
            }
        }
    }

    @Test
    fun givenRepository_whenFetchRocketsCalled_thenRocketsObjectsCreatedAndPropertiesCorrect() {
        val rocketsResponse = listOf(
            RocketResponse(
                id = "5eb87cdaffd86e000604b32b",
                name = "Mega rocket",
                type = "Super duper type"
            ),
            RocketResponse(
                id = "42udnaffd86e000604b32b",
                name = "Sad rocket",
                type = "Super terrible type"
            ),
            RocketResponse(
                id = "y2mclamcbvoa",
                name = "Skyrim rocket",
                type = "Super cool type"
            )
        )

        every { spaceXApiService.fetchRockets() } returns Single.just(rocketsResponse)

        val rockets = spaceXRepository.fetchRockets().blockingGet()
        rockets.forEachIndexed { index, rocket ->
            val rocketResponse = rocketsResponse[index]
            with(rocket) {
                assertThat(id).isEqualTo(rocketResponse.id)
                assertThat(name).isEqualTo(rocketResponse.name)
                assertThat(type).isEqualTo(rocketResponse.type)
            }
        }
    }
}