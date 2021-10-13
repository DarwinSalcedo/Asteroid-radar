package com.udacity.asteroidradar.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.*

@Dao
interface AsteroidDao {

    /** Get all asteroids from database */
    @Query("select * from DatabaseAsteroid ORDER BY closeApproachDate ASC")
    fun getAllAsteroids(): LiveData<List<DatabaseAsteroid>>

    /**
     * We don't snip the dates to day without time.
     * startDate should be without time of day to get all asteroids of day!
     * Time of endDate should have time short before end of day if data of full day is required.
     */
    @Query(
        "select * from DatabaseAsteroid WHERE closeApproachDate >= :startDate AND closeApproachDate <= :endDate ORDER BY closeApproachDate ASC"
    )
    fun getAsteroidsWithinTimeSpan(startDate: Date, endDate: Date): LiveData<List<DatabaseAsteroid>>

    /** Insert asteroids into database. Replace asteroids that already exist.*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)

    /* Delete all asteroids before [lastDate]. It uses highest precision, not days! */
    @Query("DELETE FROM DatabaseAsteroid WHERE closeApproachDate < :lastDate")
    fun deleteAllBefore(lastDate: Date): Int
    // for Database Inspector: delete from DatabaseAsteroid where closeApproachDate < 1623276000000
}