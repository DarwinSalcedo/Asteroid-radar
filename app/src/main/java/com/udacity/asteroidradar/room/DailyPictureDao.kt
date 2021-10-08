package com.udacity.asteroidradar.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.*

@Dao
interface DailyPictureDao {

    @Query("select * from DatabaseDailyPicture ORDER BY ID DESC")
    fun getAllDailyPictures(): LiveData<List<DatabaseDailyPicture>>


    @Query("select * from DatabaseDailyPicture WHERE mediaType == 'image' AND date <= :currentDate ORDER BY date DESC LIMIT 1")
    fun getLastDailyPictureWithImage(currentDate: Date): LiveData<DatabaseDailyPicture>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(picture: DatabaseDailyPicture)
}