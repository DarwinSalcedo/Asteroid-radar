package com.udacity.asteroidradar

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class DailyPicture(
    val id: String,
    val date: Date,
    val mediaType: String,
    val title: String,
    val url: String
) : Parcelable