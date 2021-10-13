package com.udacity.asteroidradar.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/** Instantiate database daily pictures (database table) */
@Entity
data class DatabaseDailyPicture constructor(
    @PrimaryKey
    val id: String,
    val date: Date,
    val mediaType: String,
    val title: String,
    val url: String
)