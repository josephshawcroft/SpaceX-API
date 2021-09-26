package com.josephshawcroft.spacexapi.data

import com.josephshawcroft.spacexapi.data.model.Rocket

class RocketBuilder(
    var id: String = "1",
    var name: String = "default name",
    var type: String = "super rocket"
) {
    fun build() = Rocket(id, name, type)
}