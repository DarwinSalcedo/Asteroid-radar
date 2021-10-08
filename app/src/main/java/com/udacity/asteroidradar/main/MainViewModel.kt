package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.DailyPicture
import com.udacity.asteroidradar.network.AsteroidsApiFilter
import com.udacity.asteroidradar.network.asDomainModel
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.room.AsteroidsDatabase
import com.udacity.asteroidradar.room.DatabaseAsteroid
import com.udacity.asteroidradar.utils.DateUtils
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class MainViewModel(application: Application) : ViewModel() {

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()
    val navigateToSelectedAsteroid: LiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid

    private val database = AsteroidsDatabase.getInstance(application)
    private val asteroidsRepository = AsteroidsRepository(database)

    var asteroids: LiveData<List<Asteroid>> =
        Transformations.map(
            database.asteroidDao.getAsteroidsWithinTimeSpan(
                DateUtils.getDateWithoutTime(),
                DateUtils.getDateOfNextDay(DateUtils.getDateWithoutTime())
            )
        ) {
            it.asDomainModel()
        }

    var dailyPictureData: LiveData<DailyPicture?> = getDailyPicture()


    private fun getDailyPicture(): LiveData<DailyPicture?> {
        val map = Transformations.map(
            database.dailyPictureDao.getLastDailyPictureWithImage(DateUtils.getDateWithoutTime())
        ) {
            it?.asDomainModel()
        }
        return map
    }

    init {
        refreshAsteroids()
        refreshDailyPicture()
    }

    /** Get data of daily image */
    private fun refreshDailyPicture() {
        viewModelScope.launch {
            try {
                Timber.i("refreshDailyPicture(): before service call ")
                asteroidsRepository.refreshDailyPicture()
                Timber.i("refreshDailyPicture(): after service call ")
            } catch (e: Exception) {
                Timber.i("refreshDailyPicture(): exception ${e.message}")
                Timber.i("refreshDailyPicture(): exception ${e.stackTrace}")
            }
        }
    }

    /** Filters asteroids using an [AsteroidsApiFilter]. */
    fun filterAsteroids(filter: AsteroidsApiFilter) {
        Timber.i("filterAsteroids(): filter: $filter")
        when (filter) {
            AsteroidsApiFilter.VIEW_TODAY_ASTEROIDS -> {
                val date = DateUtils.getDateWithoutTime()
                filterAsteroids(date, date)
            }
            AsteroidsApiFilter.VIEW_WEEK_ASTEROIDS -> {
                val startDate = DateUtils.getDateWithoutTime()
                filterAsteroids(
                    startDate,
                    DateUtils.getDate6DaysLater(startDate)
                )
            }
            else -> {   // show all saved asteroids
                filterAsteroids()
            }
        }
    }

    /**
     * Get list of asteroids. Set time span.
     * We return domain objects, which are agnostic of Network or Database.
     */
    private fun filterAsteroids(startDate: Date? = null, endDate: Date? = null) {
        asteroids = if (startDate != null && endDate != null) {
            Transformations.map(
                database.asteroidDao.getAsteroidsWithinTimeSpan(
                    startDate,
                    endDate
                )
            ) {
                it.asDomainModel()
            }
        } else {
            val databaseAsteroidsLiveData: LiveData<List<DatabaseAsteroid>> =
                database.asteroidDao.getAllAsteroids()
            Transformations.map(databaseAsteroidsLiveData) {
                it.asDomainModel()
            }
        }

        // Timber.i("filterAsteroids() at end, var asteroid contains ${asteroids.value?.count()} asteroids")
    }

    private fun refreshAsteroids() {
        viewModelScope.launch {
            try {
                Timber.i("refreshAsteroids(): before service call ")
                asteroidsRepository.refreshAsteroids()
                Timber.i("refreshAsteroids(): after service call ")
            } catch (e: Exception) {
                Timber.i("refreshAsteroids(): exception ${e.message}")
                Timber.i("refreshAsteroids(): exception ${e.stackTrace}")
            }
        }
    }

    /**
     * When the asteroid entry is clicked, set the [_navigateToSelectedAsteroid] [MutableLiveData].
     * @param asteroid The [Asteroid] that was clicked on.
     */
    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    /**
     * After the navigation has taken place, make sure [navigateToSelectedAsteroid] is set to null
     */
    fun navigateToAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

    /**
     * Factory for constructing MainViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}