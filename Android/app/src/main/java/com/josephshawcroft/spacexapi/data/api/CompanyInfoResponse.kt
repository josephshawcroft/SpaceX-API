package com.josephshawcroft.spacexapi.data.api

data class CompanyInfoResponse(
    val employees: Int,
    val founded: Int,
    val founder: String,
    val launch_sites: Int,
    val name: String,
    val valuation: Long
)