package com.udacity.asteroidradar.network

import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.DailyPicture
import com.udacity.asteroidradar.room.DatabaseAsteroid
import com.udacity.asteroidradar.room.DatabaseDailyPicture

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

/** Transform database daily pictures into domain daily . */
@JvmName("databaseDailyPicturesAsDomainModel")
fun List<DatabaseDailyPicture>.asDomainModel(): List<DailyPicture> {
    return map {
        DailyPicture(
            id = it.id,
            date = it.date,
            mediaType = it.mediaType,
            title = it.title,
            url = it.url
        )
    }
}

@JvmName("databaseDailyPicturesAsDomainModel")
fun DatabaseDailyPicture.asDomainModel(): DailyPicture {
    return DailyPicture(
        id = this.id,
        date = this.date,
        mediaType = this.mediaType,
        title = this.title,
        url = this.url
    )
}

fun List<DailyPicture>.asDatabaseModel(): Array<DatabaseDailyPicture> {
    return this.map {
        DatabaseDailyPicture(
            id = it.id,
            date = it.date,
            mediaType = it.mediaType,
            title = it.title,
            url = it.url
        )
    }.toTypedArray()
}


fun DailyPicture.asDatabaseModel(): DatabaseDailyPicture {
    return DatabaseDailyPicture(
        id = this.id,
        date = this.date,
        mediaType = this.mediaType,
        title = this.title,
        url = this.url
    )
}