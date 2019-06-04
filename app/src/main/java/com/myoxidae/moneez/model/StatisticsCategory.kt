package com.myoxidae.moneez.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

/*@Parcelize
data class StatisticsCategory(
    val name: String,
    val percentage: Double?,
    val sum: Double
) : Parcelable
*/

@Parcelize
data class StatisticsCategory(
    val categoryId: Long?,
    val name: String,
    val icon: String,
    var color: String,
    var amount: Double
) : Parcelable
