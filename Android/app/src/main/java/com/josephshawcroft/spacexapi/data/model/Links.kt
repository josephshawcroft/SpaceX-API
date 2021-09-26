package com.josephshawcroft.spacexapi.data.model

data class Links(
    val article: String,
    val flickr: Flickr,
    val patch: Patch,
    val presskit: Any,
    val reddit: Reddit,
    val webcast: String,
    val wikipedia: String,
    val youtube_id: String
)