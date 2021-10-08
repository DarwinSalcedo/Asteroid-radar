package com.udacity.asteroidradar.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class DatabaseAsteroid constructor(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: Date,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)