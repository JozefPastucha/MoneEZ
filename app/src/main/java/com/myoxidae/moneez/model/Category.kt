package com.myoxidae.moneez.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import net.steamcrafted.materialiconlib.MaterialIconView

//will use some library for money, currency, exchange
@Entity(tableName = "categories")
@Parcelize
data class Category(
    val name: String,
    val description: String,
    val icon: String,
    var color: String,
    var status: CategoryStatus
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @IgnoredOnParcel
    var categoryId: Long = 0
}