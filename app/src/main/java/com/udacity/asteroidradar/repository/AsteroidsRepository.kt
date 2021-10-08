package com.udacity.asteroidradar.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.parseAsteroids
import com.udacity.asteroidradar.network.asDatabaseModel
import com.udacity.asteroidradar.network.asDomainModel
import com.udacity.asteroidradar.room.AsteroidsDatabase
import com.udacity.asteroidradar.utils.DateUtils
import com.udacity.asteroidradar.utils.DateUtils.Companion.getCurrentDateTime
import com.udacity.asteroidradar.utils.DateUtils.Companion.toFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import com.udacity.asteroidradar.network.AsteroidApiService.AsteroidsApi.retrofitService as AsteroidService

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    companion object {
        const val ASTEROIDS_DATE_FORMAT = "yyyy-MM-dd"
    }

    /** The internal MutableLiveData string that stores the status of
    the most recent request.*/
//    private val _status = MutableLiveData<AsteroidsApiStatus>()

    val date: Date = Date()


    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }


    suspend fun refreshAsteroids() {
        val currentDate = getCurrentDateTime()
        val endDate: String =
            DateUtils.getDate6DaysLater(currentDate).toFormat(ASTEROIDS_DATE_FORMAT)
        val startDate: String = currentDate.toFormat(ASTEROIDS_DATE_FORMAT)
       // Timber.i("refreshAsteroids() before server call. startDate: $startDate, endDate: $endDate")

        withContext(Dispatchers.IO) {
            val asteroidsFullData = AsteroidService.getAsteroids(startDate, endDate)
                    as Map<*, *>
            val asteroids: List<Asteroid> = parseAsteroids(asteroidsFullData)

            database.asteroidDao.insertAll(*asteroids.asDatabaseModel())
        }
    }
}