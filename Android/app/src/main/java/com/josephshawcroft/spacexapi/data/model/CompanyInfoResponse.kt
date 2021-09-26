package com.josephshawcroft.spacexapi.data.model

data class CompanyInfo(
    val employees: Int,
    val founded: Int,
    val founder: String,
    val launchSites: Int,
    val name: String,
    val valuation: Long
)