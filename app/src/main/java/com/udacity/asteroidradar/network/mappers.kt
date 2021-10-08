package com.udacity.asteroidradar.network

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.room.DatabaseAsteroid

fun List<Asteroid>.asDatabaseModel(): Array<DatabaseAsteroid> {
    return this.map {
        DatabaseAsteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )

    }.toTypedArray()
}

/** Transform database asteroids into domain asteroids. */
fun List<DatabaseAsteroid>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            it.id,
            it.codename,
            it.closeApproachDate,
            it.absoluteMagnitude,
            it.estimatedDiameter,
            it.relativeVelocity,
            it.distanceFromEarth,
            it.isPotentiallyHazardous
        )
    }
}