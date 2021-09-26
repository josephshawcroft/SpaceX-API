package com.josephshawcroft.spacexapi.repository

import com.josephshawcroft.spacexapi.data.api.CompanyInfoResponse
import com.josephshawcroft.spacexapi.network.SpaceXApiService
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SpaceXRepositoryImplTest {

    private var spaceXApiService: SpaceXApiService = mockk()
    private var spaceXRepository = SpaceXRepositoryImpl(spaceXApiService)

    @Test
    fun givenRepository_whenFetchCompanyCalled_thenCompanyInfoCreated() {

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

    fun testFetchLaunches() {}

    fun testFetchRockets() {}
}