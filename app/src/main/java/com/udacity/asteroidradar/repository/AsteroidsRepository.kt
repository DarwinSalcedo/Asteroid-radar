package com.udacity.asteroidradar.repository


import com.udacity.asteroidradar.DailyPicture
import com.udacity.asteroidradar.api.parseAsteroids
import com.udacity.asteroidradar.api.parseDailyPicture
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.network.asDatabaseModel
import com.udacity.asteroidradar.room.AsteroidsDatabase
import com.udacity.asteroidradar.utils.DateUtils
import com.udacity.asteroidradar.utils.DateUtils.Companion.toAsteroidsDateString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import com.udacity.asteroidradar.network.AsteroidApiService.AsteroidsApi.retrofitService as AsteroidService

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    companion object {
        const val ASTEROIDS_DATE_FORMAT = "yyyy-MM-dd"
    }

    /** The internal MutableLiveData string that stores the status of
    the most recent request.*/
//    private val _status = MutableLiveData<AsteroidsApiStatus>()

    val date: Date = DateUtils.getDateWithoutTime()


    /**
     * Refresh the Asteroid data stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the I0 dispatcher. By switching to the I0 dispatcher using 'withContext this
     * function is now safe to call from any thread including the Main thread.
     *
     * To actually load the asteroids for use, observe asteroids.
     */
    suspend fun refreshAsteroids() {
        val currentDate = DateUtils.getDateWithoutTime()
        val endDate = DateUtils.getDate6DaysLater(currentDate).toAsteroidsDateString()
        val startDate: String = currentDate.toAsteroidsDateString()
        Timber.i("refreshAsteroids() before server call. startDate: $startDate, endDate: $endDate")

        withContext(Dispatchers.IO) {
            val asteroidsFullData = AsteroidService.getAsteroids(startDate, endDate)
                    as Map<*, *>
            val asteroids: List<Asteroid> = parseAsteroids(asteroidsFullData)

            database.asteroidDao.insertAll(*asteroids.asDatabaseModel())
        }

        Timber.i("refreshAsteroids() after server call. startDate: $startDate, endDate: $endDate")
    }

    suspend fun refreshDailyPicture() {
        Timber.i("refreshDailyPicture() before server call.")

        withContext(Dispatchers.IO) {
            try {
                val rawDailyPicture = AsteroidService.getDailyPictureData() as Map<*, *>
                val dailyPicture: DailyPicture = parseDailyPicture(rawDailyPicture)
                Timber.i("refreshDailyPicture() rawPictureDataAny: $rawDailyPicture")
                database.dailyPictureDao.insert(dailyPicture.asDatabaseModel())
            } catch (exc: Exception) {
                Timber.e("refreshDailyPicture()  ${exc.message}")
            }
        }

        Timber.i("refreshDailyPicture() after server call.")
    }

    suspend fun deleteAsteroidsBefore(endDate: Date) {
        Timber.i("deleteAsteroidsBefore() before database call. endDate: $endDate")

        withContext(Dispatchers.IO) {
            val deletedElementsCount: Int = database.asteroidDao.deleteAllBefore(endDate)
            Timber.i("deleteAsteroidsBefore() after server call. deleted elements: $deletedElementsCount")
        }
        Timber.i("deleteAsteroidsBefore() after server call.")
    }
}